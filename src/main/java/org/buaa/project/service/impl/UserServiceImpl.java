package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.consts.MailSendConst;
import org.buaa.project.common.convention.exception.ClientException;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.common.enums.UserErrorCodeEnum;
import org.buaa.project.dao.entity.UserDO;
import org.buaa.project.dao.mapper.UserMapper;
import org.buaa.project.dto.req.UserRegisterReqDTO;
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

import java.util.concurrent.TimeUnit;

import static org.buaa.project.common.consts.MailSendConst.EMAIL_SUFFIX;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_REGISTER_CODE_EXPIRE;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_REGISTER_CODE_KEY;
import static org.buaa.project.common.consts.RedisCacheConstant.USER_REGISTER_LOCK_KEY;
import static org.buaa.project.common.enums.ServiceErrorCodeEnum.MAIL_SEND_ERROR;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_CODE_ERROR;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_EXIST;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static org.buaa.project.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

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

}
