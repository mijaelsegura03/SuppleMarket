package com.mijaelsegura.eCommerceSpring.dto;

public abstract class Result {
    private String message;
    private boolean success;
    private String typeError;

    public Result(String message, boolean success, String typeError) {
        this.message = message;
        this.success = success;
        this.typeError = typeError;
    }

    public Result() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTypeError() {
        return typeError;
    }

    public void setTypeError(String typeError) {
        this.typeError = typeError;
    }
}
