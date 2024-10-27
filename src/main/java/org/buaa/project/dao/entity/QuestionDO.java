package org.buaa.project.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("question")
public class QuestionDO {
    /**
     * id
     */
    private Long id;

    /**
     * 分类id
     */
    private Integer category;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布人id
     */
    private Long userId;

    /**
     * 浏览量
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer viewCount;

    /**
     * 点赞数
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer likeCount;

    /**
     * 是否解决 0：未解决 1：已解决
     */
    @TableField(fill = FieldFill.INSERT)
    private int solvedFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标识 0：未删除 1：已删除
     */
    @TableField(fill = FieldFill.INSERT)
    private int delFlag;

}
