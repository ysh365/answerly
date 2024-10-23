package org.buaa.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buaa.project.dao.entity.QuestionDO;

import java.util.List;

public interface QuestionService extends IService<QuestionDO> {
    //上传题目
    Boolean uploadQuestion(int Category, String content, String title, int idUser, List<String> pictures);
}
