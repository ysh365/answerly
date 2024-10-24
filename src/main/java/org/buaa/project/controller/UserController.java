package org.buaa.project.controller;


/**
 * 用户管理控制层
 */

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.resp.UserActualRespDTO;
import org.buaa.project.dto.resp.UserRespDTO;
import org.buaa.project.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    /**
     * 根据用户名查找用户信息
     */
    @GetMapping("/api/answerly/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名查询无脱敏用户信息
     */
    @GetMapping("/api/answerly/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     */
    @GetMapping("/api/answerly/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }


    /**
     * 发送验证码
     */
    @PostMapping("/api/answerly/v1/user/send-code")
    public Result<Boolean> sendCode(@RequestParam("email") String email) {
        return Results.success(userService.sendCode(email));
    }




}

