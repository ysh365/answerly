package org.buaa.project.mq;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buaa.project.common.convention.exception.ServiceException;
import org.buaa.project.dao.entity.MessageDO;
import org.buaa.project.service.MessageService;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private final StringRedisTemplate stringRedisTemplate;

    private final MqIdempotent mqIdempotent;

    private final MessageService messageService;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String stream = message.getStream();
        RecordId id = message.getId();
        if (mqIdempotent.isMessageBeingConsumed(id.toString())) {
            // 判断当前的这个消息流程是否执行完成
            if (mqIdempotent.isAccomplish(id.toString())) {
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }
        try {
            Map<String, String> producerMap = message.getValue();
            MqEvent event = JSON.parseObject(producerMap.get("event"), MqEvent.class);
            consume(event);
            stringRedisTemplate.opsForStream().delete(Objects.requireNonNull(stream), id.getValue());
        } catch (Throwable ex) {
            // 某某某情况宕机了
            mqIdempotent.delMessageProcessed(id.toString());
            log.error("消费异常", ex);
            throw ex;
        }
        mqIdempotent.setAccomplish(id.toString());
    }

    public void consume(MqEvent event) {
        switch (event.getMessageType()) {
            case System:

                break;
            case Like:
                if (!Objects.equals(event.getUserId(), event.getEntityUserId())) {   // 自己给自己点赞不发消息
                    MessageDO messageDO = MessageDO.builder()
                            .fromId(event.getUserId())
                            .toId(event.getEntityUserId())
                            .type(event.getMessageType().toString())
                            .build();
                    messageService.save(messageDO);
                }
                break;
            case ANSWER:

                break;
            default:
                break;
        }
    }

}