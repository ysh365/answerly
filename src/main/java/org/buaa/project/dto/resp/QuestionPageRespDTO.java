package org.buaa.project.dto.resp;

import lombok.Data;

/**
 * 问题分页查询响应
 */
@Data
public class QuestionPageRespDTO {


    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否解决 0：未解决 1：已解决
     */
    private int solvedFlag;

}