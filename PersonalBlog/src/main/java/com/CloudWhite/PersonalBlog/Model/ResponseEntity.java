package com.CloudWhite.PersonalBlog.Model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResponseEntity {
    private String code = "200";
    private String message = "请求成功";
    private Object data = null;

    public ResponseEntity() {
    }

    public ResponseEntity(Object data) {
        this.data = data;
    }

    public ResponseEntity(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

