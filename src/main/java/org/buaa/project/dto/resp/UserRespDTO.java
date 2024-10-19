package org.buaa.project.dto.resp;

import lombok.Data;

import java.util.Date;

/**
 * 用户返回参数实体
 */
@Data
public class UserRespDTO {
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
     * 创建时间
     */
    private Date createdDate;
}
