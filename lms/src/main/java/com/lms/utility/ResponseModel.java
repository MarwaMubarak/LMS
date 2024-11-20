package com.lms.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel<T> {
    private String status;
    private String message;
    private T body;

    public ResponseModel(String status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }
}