package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.req.AnswerUpdateReqDTO;
import org.buaa.project.dto.req.AnswerUploadReqDTO;
import org.buaa.project.service.AnswerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 答案管理控制层
 */
@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 上传答案
     * @param requestParam
     */
    @PostMapping("/api/answerly/v1/answer")
    public Result<Void> uploadAnswer(@RequestBody AnswerUploadReqDTO requestParam) {
        answerService.uploadAnswer(requestParam);
        return Results.success();
    }

    /**
     * 修改答案
     * @param requestParam
     */
    @PutMapping("/api/answerly/v1/answer")
    public Result<Void> updateAnswer(@RequestBody AnswerUpdateReqDTO requestParam) {
        answerService.updateAnswer(requestParam);
        return Results.success();
    }

    /**
     * 删除答案
     * @param Id
     */
    @DeleteMapping("/api/answerly/v1/answer")
    public Result<Void> deleteAnswer(@RequestParam("id") Long Id) {
        answerService.deleteAnswer(Id);
        return Results.success();
    }

    /**
     * 点赞答案
     * @param Id
     */
    @PostMapping("/api/answerly/v1/answer/like")
    public Result<Void> likeQuestion(@RequestParam("id") Long Id) {
        answerService.likeAnswer(Id);
        return Results.success();
    }

    /**
     * 标记答案有用
     */
    @PostMapping("/api/answerly/v1/answer/useful")
    public Result<Void> usefulQuestion(@RequestParam("id") Long Id) {
        answerService.markUsefulAnswer(Id);
        return Results.success();
    }


}
