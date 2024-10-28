package org.buaa.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dto.req.QuestionFindReqDTO;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.dto.resp.QuestionRespDTO;

import java.util.List;

public interface QuestionService extends IService<QuestionDO> {
    //上传题目
    Boolean uploadQuestion(QuestionUploadReqDTO params);

    Boolean deleteQuestion(Long id);

    Boolean likeQuestion(Long id);

    List<QuestionRespDTO> findQuestion(QuestionFindReqDTO params);
}
