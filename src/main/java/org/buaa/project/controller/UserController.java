package org.buaa.project.controller;

/*用户管理控制层*/

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    /*根据用户名查找用户信息*/
    @GetMapping("/api/answerly/v1/user/{username}")
    public String getUser(@PathVariable String username) {
        return "Hello " + username;
    }




}
