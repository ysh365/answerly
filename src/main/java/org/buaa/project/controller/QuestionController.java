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
            @RequestBody QuestionUploadReqDTO questionUploadReqDTO
    ) {
        Boolean isUploaded = questionService.uploadQuestion(questionUploadReqDTO);
        return Results.success(isUploaded);
    }

    @DeleteMapping("/api/answerly/v1/delete/{question_id}")
    public Result<Boolean> deleteQuestion(@PathVariable("question_id") Long questionId) {
        Boolean isDeleted = questionService.deleteQuestion(questionId);
        return Results.success(isDeleted);
    }
    @PostMapping("/api/answerly/v1/like-question")
    public Result<Boolean> likeQuestion(@RequestParam("question_id")  Long questionId){
        Boolean isLiked = questionService.likeQuestion(questionId);
        return Results.success(isLiked);
    }
}

