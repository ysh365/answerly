package org.buaa.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dto.req.QuestionUploadReqDTO;

import java.util.List;

public interface QuestionService extends IService<QuestionDO> {
    //上传题目
    Boolean uploadQuestion(QuestionUploadReqDTO params);
    Boolean deleteQuestion(int id);
    Boolean likeQuestion(int id);
}
