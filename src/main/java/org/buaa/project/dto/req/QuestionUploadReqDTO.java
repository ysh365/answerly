package org.buaa.project.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
public class QuestionUploadReqDTO {
    /**
     * 包含的图片
     */
    private String images;

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
