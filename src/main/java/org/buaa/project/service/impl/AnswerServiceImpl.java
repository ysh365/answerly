package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.biz.user.UserContext;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.dao.entity.AnswerDO;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dao.mapper.AnswerMapper;
import org.buaa.project.dao.mapper.QuestionMapper;
import org.buaa.project.dto.req.AnswerUpdateReqDTO;
import org.buaa.project.dto.req.AnswerUploadReqDTO;
import org.buaa.project.service.AnswerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, AnswerDO> implements AnswerService {
    @Autowired
    private QuestionMapper questionMapper;
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
        //todo 根据questionId查询题目是否存在
        //todo 我觉得还是把answer的category字段删了比较好（）
        QuestionDO questionDO = questionMapper.selectById(reqDTO.getQuestionId());
        if (questionDO == null || questionDO.getDelFlag() == 1){
            throw  new ServiceException("问题不存在或已删除");
        }
        long userId = Long.parseLong(UserContext.getUserId());
        AnswerDO answerDO = BeanUtil.copyProperties(reqDTO, AnswerDO.class);
        answerDO.setUserId(userId);
        int inserted = baseMapper.insert(answerDO);
        return inserted > 0;

    }
    @Transactional
    @Override
    public Boolean deleteAnswer(long id){
        AnswerDO existingAnswer = baseMapper.selectById(id);
        if (existingAnswer == null) {
            throw new ServiceException("删除时答案不存在");
        }
        if(!existingAnswer.getUserId().equals(Long.parseLong(UserContext.getUserId()))){
            throw new ServiceException("没有删除权限");
        }
        existingAnswer.setDelFlag(1);
        int result = baseMapper.updateById(existingAnswer);
        return result > 0;
    }
    @Transactional
    @Override
    public Boolean markUsefulAnswer(long id){
        AnswerDO existingAnswer = baseMapper.selectById(id);
        if (existingAnswer == null) {
            throw new ServiceException("标记时答案不存在");
        }
        QuestionDO questionDO = questionMapper.selectById(existingAnswer.getQuestionId());
        if (questionDO == null) {
            throw new ServiceException("标记时答案对应问题不存在");
        }
        if(!existingAnswer.getUserId().equals(questionDO.getUserId())){
            throw new ServiceException("用户没有标记权限");
        }
        existingAnswer.setUseful(1);
        int result = baseMapper.updateById(existingAnswer);
        return result > 0;
    }
    @Override
    public Boolean updateAnswer(AnswerUpdateReqDTO requestParam) {
        //todo 也许可以加一个被标为有用之后就不允许修改的功能
        LambdaUpdateWrapper<AnswerDO> queryWrapper = Wrappers.lambdaUpdate(AnswerDO.class)
                .eq(AnswerDO::getId, requestParam.getId());
        AnswerDO answerDO = baseMapper.selectOne(queryWrapper);
        if (answerDO == null) {
            throw new ServiceException("问题不存在");
        }
        if(!String.valueOf(answerDO.getUserId()).equals(UserContext.getUserId())){
            throw new ServiceException("没有删除权限");
        }
        BeanUtils.copyProperties(requestParam, answerDO);
        int result = baseMapper.update(answerDO, queryWrapper);
        return result > 0;
    }
}
