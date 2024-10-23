package org.buaa.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.buaa.project.dao.entity.QuestionDO;
import org.buaa.project.dao.mapper.QuestionMapper;
import org.buaa.project.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionDO> implements QuestionService {
    @Transactional
    @Override
    public Boolean uploadQuestion(int category, String content, String title, int idUser, List<String> pictures){
        //todo 验证category和idUser是否在category和user表中存在
        QuestionDO question = new QuestionDO();
        question.setCategory(category);
        question.setContent(content);
        question.setTitle(title);
        question.setUserId(idUser);
        question.setViewCount(0);  // 浏览次数初始化为0
        question.setLikeCount(0);  // 点赞次数初始化为0
        question.setSolvedFlag(0);  // 问题解决标志初始化为0
        question.setCreatedDate(new Date());  // 创建时间为当前时间
        boolean isSavedQuestion = this.save(question);
        if (pictures != null && !pictures.isEmpty()) {
            //todo 进行图片处理
        }
        return isSavedQuestion;
    }
}
