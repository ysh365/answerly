package org.buaa.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("question")
public class QuestionDO {
    private int id;
    private int category;
    private String title;
    private String content;
    private int userId;
    private int viewCount;
    private int likeCount;
//
//    0:未解决,1：已解决
//
    private int solvedFlag;
    private Date createdDate;

}
