package org.buaa.project.dto.resp;

import lombok.Data;

/**
 * 用户返回参数响应
 */
@Data
public class UserActualRespDTO {

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
     * 手机号
     */
    private String phone;
}
