package org.buaa.project.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.buaa.project.dao.entity.UserDO;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.buaa.project.common.consts.RedisCacheConstants.USER_INFO_KEY;
import static org.buaa.project.common.consts.RedisCacheConstants.USER_LOGIN_EXPIRE;
import static org.buaa.project.common.consts.RedisCacheConstants.USER_LOGIN_KEY;

/**
 * 刷新 Token 过滤器
 */
@RequiredArgsConstructor
public class RefreshTokenFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = httpServletRequest.getHeader("username");
        String token = httpServletRequest.getHeader("token");
        if (StrUtil.isBlank(token) || StrUtil.isBlank(username)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String hasLogin = stringRedisTemplate.opsForValue().get(USER_LOGIN_KEY + username);
        if (StrUtil.isBlank(hasLogin) || !Objects.equals(hasLogin, token)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        UserDO userDO = JSON.parseObject(stringRedisTemplate.opsForValue().get(USER_INFO_KEY + username), UserDO.class);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setToken(token);
        userInfoDTO.setUsername(username);
        userInfoDTO.setUserId(String.valueOf(userDO.getId()));
        userInfoDTO.setUserType(userDO.getUserType());
        UserContext.setUser(userInfoDTO);

        stringRedisTemplate.expire(USER_LOGIN_KEY + username, USER_LOGIN_EXPIRE, TimeUnit.DAYS);
        
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

}