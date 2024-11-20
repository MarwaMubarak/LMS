package com.lms.utility;

public class Response {
    public static <T> ResponseModel<T> successfulResponse(String massage, T body) {
        if (body == null) {
            body = (T) "No data exist!";
        }
        return new ResponseModel<>("success", massage, body);
    }

    public static <T> ResponseModel<T> unsuccessfulResponse(String massage, T body) {
        if (body == null) {
            body = (T) "No data exist!";
        }
        return new ResponseModel<>("failure", massage, body);
    }
}
