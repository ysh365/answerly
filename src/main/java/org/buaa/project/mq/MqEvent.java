package org.buaa.project.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buaa.project.common.enums.EntityTypeEnum;
import org.buaa.project.common.enums.MessageTypeEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqEvent {

    /**
     * 消息类型
     */
    private MessageTypeEnum messageType;

    /**
     * 触发者
     */
    private Long userId;

    /**
     * 触发实体类型
     */
    private EntityTypeEnum entityType;

    /**
     * 触发实体类型
     */
    private Long entityId;

    /**
     * 实体用户ID
     */
    private Long entityUserId;

}
