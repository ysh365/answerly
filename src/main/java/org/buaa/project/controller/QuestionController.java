package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.service.QuestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        Boolean isUploaded = questionService.uploadQuestion(category, content, title, idUser, pictures);
        return Results.success(isUploaded);
    }
}

