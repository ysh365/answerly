package org.buaa.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;

import org.buaa.project.common.consts.MailSendConst;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.common.enums.UserErrorCodeEnum;
import org.buaa.project.dao.entity.UserDO;
import org.buaa.project.dao.mapper.UserMapper;
import org.buaa.project.dto.resp.UserRespDTO;
import org.buaa.project.service.UserService;
import org.buaa.project.toolkit.RandomGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final JavaMailSender mailSender;

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
    public Boolean sendCode(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = RandomGenerator.generateSixDigitCode();
        message.setFrom(from);
        message.setText(String.format(MailSendConst.TEXT, code));
        message.setTo(email);
        message.setSubject(MailSendConst.SUBJECT);
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            throw new ServiceException("邮件发送失败");
        }
    }


}
