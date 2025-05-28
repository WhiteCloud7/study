package com.WhiteCloud.SpringBootTest.Model;

public class JsonResult<T> {
    private T data;//数据
    private String code;//错误代码
    private String msg;//错误信息

    public JsonResult() {
    }

    public JsonResult(T data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
