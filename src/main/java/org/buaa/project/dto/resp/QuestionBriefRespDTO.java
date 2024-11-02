package org.buaa.project.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionBriefRespDTO {
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