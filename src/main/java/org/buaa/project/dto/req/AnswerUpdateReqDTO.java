package org.buaa.project.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnswerUpdateReqDTO {
    private long id;
    /**
     * category_id - the ID of the category/theme this answer belongs to
     */
    @JsonProperty("category_id")
    private Long categoryId;


    /**
     * content - the content of the answer, with a maximum length of 1024 characters
     */
    private String content;

    /**
     * images - paths to images associated with the answer, separated by commas, with a maximum of 9 images
     */
    private String images;
}
