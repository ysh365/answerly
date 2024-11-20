package org.buaa.project.dto.req;

import lombok.Data;

@Data
public class QuestionUpdateReqDTO {
    /**
     * 需要修改的问题id
     */
    private long id;

    /**
     * 包含的图片
     */
    private String pictures;

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
}
