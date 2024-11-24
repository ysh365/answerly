package org.buaa.project.common.enums;

import org.buaa.project.common.convention.errorcode.IErrorCode;

/**
 * 问答错误码
 */
public enum QAErrorCodeEnum implements IErrorCode {

    QUESTION_NULL("C000101", "问题不存在"),

    QUESTION_ACCESS_CONTROL_ERROR("C000102", "问题操作权限错误"),

    ANSWER_NULL("C000103", "回答不存在"),

    ANSWER_ACCESS_CONTROL_ERROR("C000104", "回答操作权限错误");

    private final String code;

    private final String message;

    QAErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
