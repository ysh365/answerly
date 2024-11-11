package org.buaa.project.controller;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.result.Result;
import org.buaa.project.common.convention.result.Results;
import org.buaa.project.dto.req.CategoryCreateReqDTO;
import org.buaa.project.dto.req.CategoryUpdateReqDTO;
import org.buaa.project.service.CategoryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主题管理控制层
 */
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
    * 增加主题
    */
    @PostMapping("/api/answerly/v1/category")
    public Result<Void> addCategory(@RequestBody CategoryCreateReqDTO requestParam) {
        categoryService.addCategory(requestParam);
        return Results.success();
    }

    /**
     * 删除主题
     */
    @DeleteMapping("/api/answerly/v1/category")
    public Result<Void> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return Results.success();
    }

    /**
     * 修改主题
     */
    @PutMapping("/api/answerly/v1/category")
    public Result<Void> updateCategory(@RequestBody CategoryUpdateReqDTO requestParam) {
        categoryService.updateCategory(requestParam);
        return Results.success();
    }

}
