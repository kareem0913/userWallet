package org.demo.userwallet.error.dto;

import org.demo.userwallet.model.ResponseEntity;

public class GlobalError implements ResponseEntity {
    private final Integer code;
    private final String message;
    private final Object errors;

    public GlobalError(Integer code, String message, Object errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getErrors() {
        return errors;
    }

}
