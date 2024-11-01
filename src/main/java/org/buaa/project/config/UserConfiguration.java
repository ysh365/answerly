package org.buaa.project.config;

import org.buaa.project.common.biz.user.LoginCheckFilter;
import org.buaa.project.common.biz.user.RefreshTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 用户配置自动装配
 */
@Configuration
public class UserConfiguration {

    /**
     * 刷新 Token 过滤器
     */
    @Bean
    public FilterRegistrationBean<RefreshTokenFilter> globalUserTransmitFilter(StringRedisTemplate stringRedisTemplate) {
        FilterRegistrationBean<RefreshTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RefreshTokenFilter(stringRedisTemplate));
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

    /**
     * 登录校验拦截器
     */
    @Bean
    public FilterRegistrationBean<LoginCheckFilter> globalLoginInterceptor() {
        FilterRegistrationBean<LoginCheckFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LoginCheckFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

}