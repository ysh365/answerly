package org.buaa.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户持久层实体
 */
@Data
@TableName("user")
public class UserDO {
    /**
     * id
     */
    private Long id;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 个人简介
     */
    private String introduction;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 解决问题的数量
     */
    private Integer solvedCount;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 状态
     */
    private int status;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 删除标识 0：未删除 1：已删除
     */
    private int delFlag;
}
