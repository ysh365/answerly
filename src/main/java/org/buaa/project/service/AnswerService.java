package org.buaa.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buaa.project.dao.entity.AnswerDO;
import org.buaa.project.dto.req.AnswerUpdateReqDTO;
import org.buaa.project.dto.req.AnswerUploadReqDTO;

public interface AnswerService extends IService<AnswerDO> {
    Boolean uploadAnswer(AnswerUploadReqDTO reqDTO);
    Boolean likeAnswer(long id) ;
    Boolean deleteAnswer(long id);
    Boolean markUsefulAnswer(long id);
    Boolean updateAnswer(AnswerUpdateReqDTO reqDTO);
}
