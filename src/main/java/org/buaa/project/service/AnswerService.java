package org.buaa.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buaa.project.dao.entity.AnswerDO;
import org.buaa.project.dto.req.AnswerMinePageReqDTO;
import org.buaa.project.dto.req.AnswerPageReqDTP;
import org.buaa.project.dto.req.AnswerUpdateReqDTO;
import org.buaa.project.dto.req.AnswerUploadReqDTO;
import org.buaa.project.dto.resp.AnswerPageRespDTO;

public interface AnswerService extends IService<AnswerDO> {

    /**
     * 上传回答
     * @param reqDTO
     */
    void uploadAnswer(AnswerUploadReqDTO reqDTO);

    /**
     * 点赞回答
     * @param id
     */
    void likeAnswer(long id, long entityUserId); ;

    /**
     * 删除回答
     * @param id
     */
    void deleteAnswer(long id);

    /**
     * 标记回答有用
     * @param id
     */
    void markUsefulAnswer(long id);

    /**
     * 更新回答
     * @param reqDTO
     */
    void updateAnswer(AnswerUpdateReqDTO reqDTO);

    /**
     * 检查回答是否存在
     * @param id
     */
    void checkAnswerExist(long id);

    /**
     * 检查回答所有者
     * @param id
     */
    void checkAnswerOwner(long id);

    /**
     * 分页查询回答
     * @param requestParam
     * @return
     */
    IPage<AnswerPageRespDTO> pageAnswer(AnswerPageReqDTP requestParam);

    /**
     * 分页查询我的回答
     * @param requestParam
     * @return
     */
    IPage<AnswerPageRespDTO> pageMyAnswer(AnswerMinePageReqDTO requestParam);


}
