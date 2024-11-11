package org.buaa.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.buaa.project.common.database.BaseDO;

/**
 * 主题类别持久层实体
 */
@Data
@Builder
@TableName("category")
public class CategoryDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 排序
     */
    private Integer sort;

}
