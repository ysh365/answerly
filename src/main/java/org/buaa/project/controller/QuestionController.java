package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.req.QuestionFindReqDTO;
import org.buaa.project.dto.req.QuestionUpdateReqDTO;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.dto.resp.QuestionBriefRespDTO;
import org.buaa.project.dto.resp.QuestionRespDTO;
import org.buaa.project.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    //上传题目
    @PostMapping("/api/answerly/v1/question")
    public Result<Boolean> uploadQuestion(
            @RequestBody QuestionUploadReqDTO questionUploadReqDTO
    ) {
        Boolean isUploaded = questionService.uploadQuestion(questionUploadReqDTO);
        return Results.success(isUploaded);
    }

    //删除题目
    @DeleteMapping("/api/answerly/v1/question")
    public Result<Boolean> deleteQuestion(@RequestParam("question_id") Long questionId) {
        Boolean isDeleted = questionService.deleteQuestion(questionId);
        return Results.success(isDeleted);
    }

    //点赞题目
    @PostMapping("/api/answerly/v1/question/like")
    public Result<Boolean> likeQuestion(@RequestParam("question_id") Long questionId) {
        Boolean isLiked = questionService.likeQuestion(questionId);
        return Results.success(isLiked);
    }

    //查询题目信息流
    @GetMapping("/api/answerly/v1/question/stream")
    public Result<List<QuestionBriefRespDTO>> findQuestion(
            @RequestBody QuestionFindReqDTO questionFindReqDTO
    ) {
        return Results.success(questionService.findQuestion(questionFindReqDTO));
    }

    //查询热门10道题
    @GetMapping("/api/answerly/v1/top-ten/{category}")
    public Result<List<QuestionBriefRespDTO>> findTopTenQuestion(@PathVariable("category") int category) {
        return Results.success(questionService.findHotQuestion(category));
    }

    //点进题目查询
    @GetMapping("/api/answerly/v1/question/{id}")
    public Result<QuestionRespDTO> findQuestionById(@PathVariable("id") Long id) {
        return Results.success(questionService.findQuestionById(id));
    }
    //更新题目
    @PutMapping("/api/answerly/v1/question")
    public Result<Boolean> updateQuestion(@RequestBody QuestionUpdateReqDTO questionUpdateReqDTO) {
        Boolean isUpdated = questionService.updateQuestion(questionUpdateReqDTO);
        return Results.success(isUpdated);
    }
}

