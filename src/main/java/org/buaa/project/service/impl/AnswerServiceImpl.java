package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.biz.user.UserContext;
import org.buaa.project.common.convention.exception.ClientException;
import org.buaa.project.dao.entity.AnswerDO;
import org.buaa.project.dao.mapper.AnswerMapper;
import org.buaa.project.dto.req.AnswerUpdateReqDTO;
import org.buaa.project.dto.req.AnswerUploadReqDTO;
import org.buaa.project.service.AnswerService;
import org.buaa.project.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static org.buaa.project.common.enums.QAErrorCodeEnum.ANSWER_ACCESS_CONTROL_ERROR;
import static org.buaa.project.common.enums.QAErrorCodeEnum.ANSWER_NULL;

/**
 * 回答接口实现层
 */
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, AnswerDO> implements AnswerService {

    private final QuestionService questionService;

    @Override
    public void likeAnswer(long id){
        checkAnswerExist(id);

        AnswerDO answerDO = baseMapper.selectById(id);
        int curLikeCount = answerDO.getLikeCount();
        answerDO.setLikeCount(curLikeCount + 1);
        baseMapper.updateById(answerDO);
    }

    @Override
    public void uploadAnswer(AnswerUploadReqDTO reqDTO){
        long userId = Long.parseLong(UserContext.getUserId());
        AnswerDO answerDO = BeanUtil.copyProperties(reqDTO, AnswerDO.class);
        answerDO.setUserId(userId);
        baseMapper.insert(answerDO);
    }

    @Override
    public void deleteAnswer(long id){
        checkAnswerExist(id);
        checkAnswerOwner(id);

        AnswerDO answerDO = baseMapper.selectById(id);
        answerDO.setDelFlag(1);
        baseMapper.updateById(answerDO);
    }

    @Override
    public void markUsefulAnswer(long id){
        checkAnswerExist(id);

        AnswerDO answerDO = baseMapper.selectById(id);
        questionService.checkQuestionOwner(answerDO.getQuestionId());
        answerDO.setUseful(1);
        baseMapper.updateById(answerDO);
    }

    @Override
    public void updateAnswer(AnswerUpdateReqDTO requestParam) {
        //todo 也许可以加一个被标为有用之后就不允许修改的功能
        checkAnswerExist(requestParam.getId());
        checkAnswerOwner(requestParam.getId());

        LambdaUpdateWrapper<AnswerDO> queryWrapper = Wrappers.lambdaUpdate(AnswerDO.class)
                .eq(AnswerDO::getId, requestParam.getId());
        AnswerDO answerDO = baseMapper.selectOne(queryWrapper);
        BeanUtils.copyProperties(requestParam, answerDO);
        baseMapper.update(answerDO, queryWrapper);
    }

    @Override
    public void checkAnswerExist(long id) {
        AnswerDO answer = baseMapper.selectById(id);
        if (answer == null || answer.getDelFlag() != 0) {
            throw new ClientException(ANSWER_NULL);
        }
    }

    @Override
    public void checkAnswerOwner(long id) {
        AnswerDO answer = baseMapper.selectById(id);
        String userId = UserContext.getUserId();
        if (!answer.getUserId().equals(Long.valueOf(userId))) {
            throw new ClientException(ANSWER_ACCESS_CONTROL_ERROR);
        }
    }

}
