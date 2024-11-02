package org.buaa.project.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionRespDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 分类id
     */
    private Integer category;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布人id
     */
    private Long userId;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否解决 0：未解决 1：已解决
     */
    private int solvedFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
