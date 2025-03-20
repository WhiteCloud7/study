package com.WhiteCloud.SpringBootTest.Model;

public class MyException extends Throwable{
    private String code = "1000";
    private String msg = "这是我的异常！";

    public MyException() {
    }

    public MyException(String code, String msg) {
        this.code = code;
        this.msg = msg;
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
