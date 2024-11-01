package com.viaas.certification.exception;

public enum CertificationCode {

    USER_OR_PASSWORD_ERROR("U400","用户或密码不正确"),
    USER_EXIST("U401","用户已存在"),
    USER_INTEGRITY_ERROR("U501","账号或密码缺失"),
    NOT_FIND("404","找不到");
    private String code;
    private String message;
    CertificationCode(String code, String message) {
        this.code = code;
        this.message = message;
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
}
