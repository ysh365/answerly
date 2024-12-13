package org.buaa.project.service;

import org.buaa.project.common.enums.EntityTypeEnum;

/**
 * 点赞接口层
 */
public interface LikeService {

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     */
    void like(String userId, EntityTypeEnum entityType, long entityId, String entityUserId);

    /**
     * 查询实体点赞数量
     * @param entityType
     * @param entityId
     * @return
     */
    int findEntityLikeCount(EntityTypeEnum entityType, long entityId);

    /**
     * 查询实体点赞状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    String findEntityLikeStatus(String userId, EntityTypeEnum entityType, long entityId);

    /**
     * 查询用户点赞数量
     * @param userId
     * @return
     */
    int findUserLikeCount(String userId);
}
