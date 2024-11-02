package org.buaa.project.dto.req;

import lombok.Data;

@Data
public class QuestionFindReqDTO {
    /**
     * 分类id
     */
    private Integer category;

    /**
     * 页号
     */
    private int page;

    /**
     * 是否解决 0：未解决 1：已解决
     */
    private int solvedFlag;
}
