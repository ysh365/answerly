package org.buaa.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dao.mapper.QuestionMapper;
import org.buaa.project.dto.req.QuestionUploadReqDTO;
import org.buaa.project.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * The type Question service.
 */
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionDO> implements QuestionService {

    /**
     * 上传题目
     * @param params
     * @return
     */
    @Transactional
    @Override
    public Boolean uploadQuestion(QuestionUploadReqDTO params){
        //todo 验证category和idUser是否在category和user表中存在
        QuestionDO question = new QuestionDO();
        question.setCategory(params.getCategory());
        question.setContent(params.getContent());
        question.setTitle(params.getTitle());
        question.setUserId(params.getIdUser());
        question.setViewCount(0);  // 浏览次数初始化为0
        question.setLikeCount(0);  // 点赞次数初始化为0
        question.setSolvedFlag(0);  // 问题解决标志初始化为0
        question.setDelFlag(0);
        question.setCreatedDate(new Date());  // 创建时间为当前时间
        boolean isSavedQuestion = this.save(question);
        if (params.getPictures() != null && !params.getPictures().isEmpty()) {
            //todo 进行图片处理
        }
        return isSavedQuestion;
    }

    /**
     * 删除题目
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteQuestion(int id){
//        int res = baseMapper.deleteById(id);
//        return res > 0;
        QuestionDO question = new QuestionDO();
        question.setId(id);
        question.setDelFlag(1);
        int result = baseMapper.updateById(question);
        return result > 0;
    }

    /**
     * 点赞题目
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Boolean likeQuestion(int id){
        QuestionDO question = baseMapper.selectById(id);
        if (question == null) {
            throw new ServiceException("问题不存在");
        }
        int curLikeCount = question.getLikeCount();
        question.setLikeCount(curLikeCount + 1);
        int result = baseMapper.updateById(question);
        return result > 0;
    }

}
