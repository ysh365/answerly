package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.biz.user.UserContext;
import org.buaa.project.common.consts.MailSendConst;
import org.buaa.project.common.convention.exception.ClientException;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.common.enums.UserErrorCodeEnum;
import org.buaa.project.dao.entity.UserDO;
import org.buaa.project.dao.mapper.UserMapper;
import org.buaa.project.dto.req.UserLoginReqDTO;
import org.buaa.project.dto.req.UserRegisterReqDTO;
import org.buaa.project.dto.req.UserUpdateReqDTO;
import org.buaa.project.dto.resp.UserLoginRespDTO;
import org.buaa.project.dto.resp.UserRespDTO;
import org.buaa.project.service.UserService;
import org.buaa.project.toolkit.RandomGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.buaa.project.common.consts.MailSendConst.EMAIL_SUFFIX;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_LOGIN_EXPIRE;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_LOGIN_KEY;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_REGISTER_CODE_EXPIRE;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_REGISTER_CODE_KEY;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_REGISTER_LOCK_KEY;
import static org.buaa.project.common.enums.ServiceErrorCodeEnum.MAIL_SEND_ERROR;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_CODE_ERROR;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_EXIST;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_NAME_NULL;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_PASSWORD_ERROR;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_REPEATED_LOGIN;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_TOKEN_NULL;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_UPDATE_ERROR;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final JavaMailSender mailSender;

    private final RedissonClient redissonClient;

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        return userDO != null;
    }

    @Override
    public Boolean sendCode(String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = RandomGenerator.generateSixDigitCode();
        message.setFrom(from);
        message.setText(String.format(MailSendConst.TEXT, code));
        message.setTo(mail);
        message.setSubject(MailSendConst.SUBJECT);
        try {
            mailSender.send(message);
            String key = USER_REGISTER_CODE_KEY + mail.replace(EMAIL_SUFFIX,"");
            stringRedisTemplate.opsForValue().set(key, code, USER_REGISTER_CODE_EXPIRE, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            throw new ServiceException(MAIL_SEND_ERROR);
        }
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        String code = requestParam.getCode();
        String key = USER_REGISTER_CODE_KEY + requestParam.getMail().replace(EMAIL_SUFFIX,"");
        String cacheCode = stringRedisTemplate.opsForValue().get(key);
        if (!code.equals(cacheCode)) {
            throw new ClientException(USER_CODE_ERROR);
        }
        if (hasUsername(requestParam.getUsername())) {
            throw new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(USER_REGISTER_LOCK_KEY + requestParam.getUsername());
        if (!lock.tryLock()) {
            throw new ClientException(USER_NAME_EXIST);
        }
        try {
            int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            if (inserted < 1) {
                throw new ClientException(USER_SAVE_ERROR);
            }
        } catch (DuplicateKeyException ex) {
            throw new ClientException(USER_EXIST);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        if (!hasUsername(requestParam.getUsername())) {
            throw new ClientException(USER_NAME_NULL);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (!Objects.equals(userDO.getPassword(), requestParam.getPassword())) {
            throw new ClientException(USER_PASSWORD_ERROR);
        }
        /**
         * Hash
         * Key：user:login:username
         * Value：
         *  Key：token标识
         *  Val：JSON 字符串（用户信息）
         */
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + requestParam.getUsername());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            throw new ClientException(USER_REPEATED_LOGIN);
        }

        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY + requestParam.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(USER_LOGIN_KEY + requestParam.getUsername(), USER_LOGIN_EXPIRE, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + username);
        if (CollUtil.isEmpty(hasLoginMap)) {
            return false;
        }
        return hasLoginMap.containsKey(token);
    }

    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)) {
            stringRedisTemplate.delete(USER_LOGIN_KEY + username);
            return;
        }
        throw new ClientException(USER_TOKEN_NULL);
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        if (!Objects.equals(requestParam.getOldUsername(), UserContext.getUsername())) {
            throw new ClientException(USER_UPDATE_ERROR);
        }
        if (!requestParam.getOldUsername().equals(requestParam.getNewUsername()) && hasUsername(requestParam.getNewUsername())) {
            throw new ClientException(USER_NAME_EXIST);
        }
        UserDO userDO = new UserDO();
        userDO.setUsername(requestParam.getNewUsername());
        userDO.setIntroduction(requestParam.getIntroduction());
        userDO.setPhone(requestParam.getPhone());
        userDO.setPassword(requestParam.getPassword());
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
            .eq(UserDO::getUsername, requestParam.getOldUsername());
        baseMapper.update(userDO, updateWrapper);
        /**
         * 更新redis缓存
         */
        if (!requestParam.getOldUsername().equals(requestParam.getNewUsername())) {
            Object userInfoJsonStr = stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + requestParam.getOldUsername(), UserContext.getToken());
            if (userInfoJsonStr != null) {
                stringRedisTemplate.delete(USER_LOGIN_KEY + requestParam.getOldUsername());
                stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY + requestParam.getNewUsername(), UserContext.getToken(), JSON.toJSONString(userInfoJsonStr));
                stringRedisTemplate.expire(USER_LOGIN_KEY + requestParam.getNewUsername(), USER_LOGIN_EXPIRE, TimeUnit.DAYS);
            } else {
                throw new ServiceException(USER_UPDATE_ERROR);
            }
        }
    }


}
