package org.buaa.project.common.enums;

/**
 * 消息类型枚举
 */
public enum MessageTypeEnum {

    System("system"),

    Like("like"),

    ANSWER("answer");

    private final String type;

    MessageTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
