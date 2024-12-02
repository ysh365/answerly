package org.buaa.project.dto.resp;

import lombok.Data;

/**
 * 主题类别返回实体对象
 */
@Data
public class CategoryRespDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 主题名称
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
