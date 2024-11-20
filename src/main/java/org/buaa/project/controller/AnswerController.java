package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.req.AnswerUpdateReqDTO;
import org.buaa.project.dto.req.AnswerUploadReqDTO;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.service.AnswerService;
import org.buaa.project.service.QuestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    //上传答案
    @PostMapping("/api/answerly/v1/answer")
    public Result<Boolean> uploadAnswer(
            @RequestBody AnswerUploadReqDTO answerUploadReqDTO
            ) {
        Boolean isUploaded = answerService.uploadAnswer(answerUploadReqDTO);
        return Results.success(isUploaded);
    }

    //删除答案
    @DeleteMapping("/api/answerly/v1/answer")
    public Result<Boolean> deleteAnswer(@RequestParam("answer_id") Long answerId) {
        Boolean isDeleted = answerService.deleteAnswer(answerId);
        return Results.success(isDeleted);
    }

    //点赞答案
    @PostMapping("/api/answerly/v1/answer/like")
    public Result<Boolean> likeQuestion(@RequestParam("answer_id") Long answerId) {
        Boolean isLiked = answerService.likeAnswer(answerId);
        return Results.success(isLiked);
    }

    //标记答案有用
    @PostMapping("/api/answerly/v1/answer/useful")
    public Result<Boolean> usefulQuestion(@RequestParam("answer_id") Long answerId) {
        Boolean res = answerService.markUsefulAnswer(answerId);
        return Results.success(res);
    }

    //更新答案
    @PutMapping("/api/answerly/v1/answer")
    public Result<Boolean> updateAnswer(@RequestBody AnswerUpdateReqDTO answerUpdateReqDTO) {
        Boolean isUpdated = answerService.updateAnswer(answerUpdateReqDTO);
        return Results.success(isUpdated);
    }
}
