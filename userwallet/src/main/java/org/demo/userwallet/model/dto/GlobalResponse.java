package org.demo.userwallet.model.dto;

import org.demo.userwallet.model.ResponseEntity;

public class GlobalResponse implements ResponseEntity {
    private final Integer code;
    private final String message;
    private final Object data;

    public GlobalResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
