package org.buaa.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.buaa.project.common.enums.EntityTypeEnum;
import org.buaa.project.service.LikeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.buaa.project.common.consts.RedisCacheConstants.PREFIX_ENTITY_LIKE;

/**
 * 点赞接口实现层
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void like(String userId, EntityTypeEnum entityType, long entityId, String entityUserId) {
        String entityLikeKey = String.format(PREFIX_ENTITY_LIKE, entityType, entityId);
        String userLikeKey = String.format(PREFIX_ENTITY_LIKE, EntityTypeEnum.USER, entityUserId);
        boolean isMember = Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(entityLikeKey, userId));
        if (isMember) {
            stringRedisTemplate.opsForSet().remove(entityLikeKey, userId);
            stringRedisTemplate.opsForValue().decrement(userLikeKey);
        } else {
            stringRedisTemplate.opsForSet().add(entityLikeKey, userId);
            stringRedisTemplate.opsForValue().increment(userLikeKey);
        }
    }

    @Override
    public int findEntityLikeCount(EntityTypeEnum entityType, long entityId) {
        String entityLikeKey = String.format(PREFIX_ENTITY_LIKE, entityType, entityId);
        Long size = stringRedisTemplate.opsForSet().size(entityLikeKey);
        return size != null ? size.intValue() : 0;
    }

    @Override
    public String findEntityLikeStatus(String userId, EntityTypeEnum entityType, long entityId) {
        String entityLikeKey = String.format(PREFIX_ENTITY_LIKE, entityType, entityId);
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(entityLikeKey, userId)) ? "已点赞" : "未点赞";
    }

    @Override
    public int findUserLikeCount(String userId) {
        String entityLikeKey = String.format(PREFIX_ENTITY_LIKE, EntityTypeEnum.USER, userId);
        String size = stringRedisTemplate.opsForValue().get(entityLikeKey);
        return size != null ? Integer.parseInt(size) : 0;
    }


}
