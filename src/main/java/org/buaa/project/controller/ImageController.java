package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.service.ImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片管理控制层
 */
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * 上传图片
     */
    @PostMapping("/api/answerly/v1/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return Results.success(imageService.uploadImage(file));
    }

}
