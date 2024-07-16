package com.viaas.certification.exception;

public class CertificationException extends RuntimeException{
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(String code,String message){
        this.code = code;
        this.message = message;
    }

    public CertificationException(CertificationCode certificationCode){
        this.code = certificationCode.getCode();
        this.message = certificationCode.getMessage();
    }

    public CertificationException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CertificationException(String message, String code, String message1) {
        super(message);
        this.code = code;
        this.message = message1;
    }

    public CertificationException(String message, Throwable cause, String code, String message1) {
        super(message, cause);
        this.code = code;
        this.message = message1;
    }
}
