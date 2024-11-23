package org.buaa.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.req.QuestionPageReqDTO;
import org.buaa.project.dto.req.QuestionUpdateReqDTO;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.dto.resp.QuestionPageRespDTO;
import org.buaa.project.dto.resp.QuestionRespDTO;
import org.buaa.project.service.QuestionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 图片管理控制层
 */
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 上传题目
     */
    @PostMapping("/api/answerly/v1/question")
    public Result<Void> uploadQuestion(@RequestBody QuestionUploadReqDTO questionUploadReqDTO) {
        questionService.uploadQuestion(questionUploadReqDTO);
        return Results.success();
    }

    /**
     * 修改题目
     */
    @PutMapping("/api/answerly/v1/question")
    public Result<Void> updateQuestion(@RequestBody QuestionUpdateReqDTO questionUpdateReqDTO) {
        questionService.updateQuestion(questionUpdateReqDTO);
        return Results.success();
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/api/answerly/v1/question")
    public Result<Void> deleteQuestion(@RequestParam("id") Long Id) {
        questionService.deleteQuestion(Id);
        return Results.success();
    }

    /**
     * 点赞题目
     */
    @PostMapping("/api/answerly/v1/question/like")
    public Result<Void> likeQuestion(@RequestParam("id") Long Id) {
        questionService.likeQuestion(Id);
        return Results.success();
    }

    /**
     * 标记问题已经解决
     */
    @PostMapping("/api/answerly/v1/question/resolved")
    public Result<Void> resolvedQuestion(@RequestParam("id") Long Id) {
        questionService.resolvedQuestion(Id);
        return Results.success();
    }

    /**
     * 分页查询题目
     */
    @GetMapping("/api/answerly/v1/question/page")
    public Result<IPage<QuestionPageRespDTO>> pageQuestion(QuestionPageReqDTO requestParam) {
        return Results.success(questionService.pageQuestion(requestParam));
    }

    /**
     * 查询热门题目
     */
    @GetMapping("/api/answerly/v1/top-ten/{category}")
    public Result<List<QuestionPageRespDTO>> findTopTenQuestion(@PathVariable("category") int category) {
        return Results.success(questionService.findHotQuestion(category));
    }

    /**
     * 查询题目详情
     */
    @GetMapping("/api/answerly/v1/question/{id}")
    public Result<QuestionRespDTO> findQuestionById(@PathVariable("id") Long id) {
        return Results.success(questionService.findQuestionById(id));
    }


}

