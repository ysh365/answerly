package org.buaa.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buaa.project.dao.entity.CategoryDO;
import org.buaa.project.dto.req.CategoryCreateReqDTO;

/**
 * 主题类别接口层
 */
public interface CategoryService extends IService<CategoryDO> {

    /**
     * 增加主题
     */
    public void addCategory(CategoryCreateReqDTO requestParam);
}
