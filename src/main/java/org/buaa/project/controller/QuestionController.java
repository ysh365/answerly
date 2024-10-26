package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/api/answerly/v1/uplode-question")
    public Result<Boolean> uploadQuestion(
            @RequestParam("category") Integer category,
            @RequestParam("content") String content,
            @RequestParam("title") String title,
            @RequestParam("id_user") Integer idUser,
            @RequestParam(value = "pictures", required = false) List<String> pictures // 图片可以是可选的
    ) {
        QuestionUploadReqDTO params = new QuestionUploadReqDTO(category, content, idUser, pictures, title);
        Boolean isUploaded = questionService.uploadQuestion(params);
        return Results.success(isUploaded);
    }

    @DeleteMapping("/api/answerly/v1/delete/{question_id}")
    public Result<Boolean> deleteQuestion(@PathVariable("question_id") Integer questionId) {
        Boolean isDeleted = questionService.deleteQuestion(questionId);
        return Results.success(isDeleted);
    }
    @PostMapping("/api/answerly/v1/like-question")
    public Result<Boolean> likeQuestion(@RequestParam("question_id")  Integer questionId){
        Boolean isLiked = questionService.likeQuestion(questionId);
        return Results.success(isLiked);
    }
}

