package org.buaa.project.dto.req;

import lombok.Data;

import java.util.List;

/**
 * The type Question upload req dto.
 */
@Data
public class QuestionUploadReqDTO {
    public QuestionUploadReqDTO(int category, String content, int idUser, List<String> pictures, String title) {
        this.category = category;
        this.content = content;
        this.idUser = idUser;
        this.pictures = pictures;
        this.title = title;
    }

    private int category;
    private String content;
    private String title;
    private int idUser;
    private List<String> pictures;
}
