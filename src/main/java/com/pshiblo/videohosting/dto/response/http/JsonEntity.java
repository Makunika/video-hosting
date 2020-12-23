package com.pshiblo.videohosting.dto.response.http;


import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonEntity<T> {

    @JsonProperty(value = "success")
    private boolean isSuccess;

    @JsonProperty
    private T data;

    @JsonProperty(value = "error")
    private String errorMessage;

    public JsonEntity(boolean isSuccess, T value, String errorMessage) {
        this.isSuccess = isSuccess;
        this.data = value;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
