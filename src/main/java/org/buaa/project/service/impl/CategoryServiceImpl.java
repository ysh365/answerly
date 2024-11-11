package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.dao.entity.CategoryDO;
import org.buaa.project.dao.mapper.CategoryMapper;
import org.buaa.project.dto.req.CategoryCreateReqDTO;
import org.buaa.project.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * 主题类别接口实现层
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {

    /**
     * 增加主题
     */
    @Override
    public void addCategory(CategoryCreateReqDTO requestParam) {
        baseMapper.insert(BeanUtil.toBean(requestParam, CategoryDO.class));
    }

}
