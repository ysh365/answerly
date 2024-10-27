package org.buaa.project.common.consts;

/**
 * Redis缓存常量
 */
public class RedisCacheConstant {

    /**
     * 用户注册分布式锁
     */
    public static final String USER_REGISTER_LOCK_KEY = "user:register:lock";

    /**
     * 用户注册验证码缓存
     */
    public static final String USER_REGISTER_CODE_KEY = "user:register:code";

    /**
     * 用户注册验证码缓存过期时间
     */
    public static final long USER_REGISTER_CODE_EXPIRE = 5L;

    /**
     * 用户登录缓存标识
     */
    public static final String USER_LOGIN_KEY = "user:login:";

}
