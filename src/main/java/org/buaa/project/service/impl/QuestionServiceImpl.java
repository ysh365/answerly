package org.buaa.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.biz.user.UserContext;
import org.buaa.project.common.convention.exception.ClientException;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dao.mapper.QuestionMapper;
import org.buaa.project.dto.req.QuestionPageReqDTO;
import org.buaa.project.dto.req.QuestionUpdateReqDTO;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.dto.resp.QuestionPageRespDTO;
import org.buaa.project.dto.resp.QuestionRespDTO;
import org.buaa.project.service.QuestionService;
import org.buaa.project.toolkit.CustomIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.buaa.project.common.enums.QAErrorCodeEnum.QUESTION_ACCESS_CONTROL_ERROR;
import static org.buaa.project.common.enums.QAErrorCodeEnum.QUESTION_NULL;

/**
 * The type Question service.
 */
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionDO> implements QuestionService {

    @Override
    public void uploadQuestion(QuestionUploadReqDTO requestParam) {
        QuestionDO question = BeanUtil.toBean(requestParam, QuestionDO.class);
        question.setUserId(Long.valueOf(UserContext.getUserId()));
        question.setUsername(UserContext.getUsername());
        question.setId(CustomIdGenerator.getId());
        baseMapper.insert(question);
    }

    @Override
    public void updateQuestion(QuestionUpdateReqDTO requestParam){
        Long id = requestParam.getId();
        checkQuestionExist(id);
        checkQuestionOwner(id);

        LambdaUpdateWrapper<QuestionDO> queryWrapper = Wrappers.lambdaUpdate(QuestionDO.class)
                .eq(QuestionDO::getId, requestParam.getId());
        QuestionDO questionDO = baseMapper.selectOne(queryWrapper);
        BeanUtils.copyProperties(requestParam, questionDO);
        baseMapper.update(questionDO, queryWrapper);
    }

    @Override
    public void deleteQuestion(Long id) {
        checkQuestionExist(id);
        if(!UserContext.getUserType().equals("admin")){
            checkQuestionOwner(id);
        }

        QuestionDO question = baseMapper.selectById(id);
        question.setDelFlag(1);
        baseMapper.updateById(question);
    }

    @Override
    public void likeQuestion(Long id) {
        checkQuestionExist(id);

        QuestionDO question = baseMapper.selectById(id);
        int curLikeCount = question.getLikeCount();
        question.setLikeCount(curLikeCount + 1);
        baseMapper.updateById(question);
    }

    @Override
    public void resolvedQuestion(Long id) {
        checkQuestionExist(id);
        checkQuestionOwner(id);

        QuestionDO question = baseMapper.selectById(id);
        question.setSolvedFlag(1);
        baseMapper.updateById(question);
    }

    @Override
    public QuestionRespDTO findQuestionById(Long id) {
        QuestionDO question = baseMapper.selectById(id);
        QuestionRespDTO result = new QuestionRespDTO();
        BeanUtils.copyProperties(question, result);
        return result;
    }

    @Override
    public IPage<QuestionPageRespDTO> pageQuestion(QuestionPageReqDTO requestParam) {
        LambdaQueryWrapper<QuestionDO> queryWrapper = Wrappers.lambdaQuery(QuestionDO.class)
                    .eq(QuestionDO::getDelFlag, 0)
                    .eq(requestParam.getSolvedFlag() != 2 , QuestionDO::getSolvedFlag, requestParam.getSolvedFlag())
                    .eq(QuestionDO::getCategoryId, requestParam.getCategoryId());
        IPage<QuestionDO> page = baseMapper.selectPage(requestParam, queryWrapper);
        return page.convert(each -> BeanUtil.toBean(each, QuestionPageRespDTO.class));
    }

    @Override
    public List<QuestionPageRespDTO> findHotQuestion(int category) {
        //todo 修改热度值计算，暂时先根据like_count和view_count排序
        LambdaQueryWrapper<QuestionDO> wrapper = Wrappers.lambdaQuery(QuestionDO.class)
                .eq(QuestionDO::getCategoryId, category)
                .orderByDesc(QuestionDO::getLikeCount)
                .orderByDesc(QuestionDO::getViewCount)
                .last("LIMIT 10");
        List<QuestionDO> questionDOList = baseMapper.selectList(wrapper);

        return questionDOList.stream()
                .map(questionDO -> {
                    QuestionPageRespDTO dto = new QuestionPageRespDTO();
                    BeanUtils.copyProperties(questionDO, dto);
                    return dto;
                })
                .toList();
    }

    public void checkQuestionExist(Long id) {
        QuestionDO question = baseMapper.selectById(id);
        if (question == null || question.getDelFlag() != 0) {
            throw new ClientException(QUESTION_NULL);
        }
    }

    public void checkQuestionOwner(Long id) {
        QuestionDO question = baseMapper.selectById(id);
        String userId = UserContext.getUserId();
        if (!question.getUserId().equals(Long.valueOf(userId))) {
            throw new ClientException(QUESTION_ACCESS_CONTROL_ERROR);
        }
    }

}
