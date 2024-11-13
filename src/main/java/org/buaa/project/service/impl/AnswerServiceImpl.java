package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.dao.entity.AnswerDO;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dao.mapper.AnswerMapper;
import org.buaa.project.dto.req.AnswerUploadReqDTO;
import org.buaa.project.service.AnswerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, AnswerDO> implements AnswerService {
    @Transactional
    @Override
    public Boolean likeAnswer(long id){
        AnswerDO answerDO = baseMapper.selectById(id);
        if (answerDO == null){
            throw new ServiceException("回答不存在");
        }
        int curLikeCount = answerDO.getLikeCount();
        answerDO.setLikeCount(curLikeCount + 1);
        int result = baseMapper.updateById(answerDO);
        return result > 0;
    }
    @Transactional
    @Override
    public Boolean uploadAnswer(AnswerUploadReqDTO reqDTO){
        long category = reqDTO.getCategoryId();
        long questionId = reqDTO.getQuestionId();
        long userId = reqDTO.getUserId();
        //todo 验证外键
        int inserted = baseMapper.insert(BeanUtil.toBean(reqDTO, AnswerDO.class));
        return inserted > 0;

    }
    @Transactional
    @Override
    public Boolean deleteAnswer(long id){
        AnswerDO existingAnswer = baseMapper.selectById(id);
        if (existingAnswer == null) {
            throw new ServiceException("删除时答案不存在");
        }
        existingAnswer.setDelFlag(1);
        int result = baseMapper.updateById(existingAnswer);
        return result > 0;
    }
}
