package com.mylsaber.document.domain;

import lombok.Data;

/**
 * @author mylsaber
 */
@Data
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> ResponseResult<T> success() {
        return new Builder<T>().code(200).message("success").build();
    }

    public static <T> ResponseResult<T> success(T data) {
        return new Builder<T>().code(200).message("success").data(data).build();
    }

    public static <T> ResponseResult<T> fail() {
        return new Builder<T>().code(500).message("fail").build();
    }

    public static <T> ResponseResult<T> fail(T data) {
        return new Builder<T>().code(500).message("fail").data(data).build();
    }

    public static class Builder<T> {
        private final ResponseResult<T> responseResult;

        private Builder() {
            responseResult = new ResponseResult<>();
        }

        public Builder<T> code(Integer code) {
            responseResult.setCode(code);
            return this;
        }

        public Builder<T> message(String message) {
            responseResult.setMessage(message);
            return this;
        }

        public Builder<T> data(T data) {
            responseResult.setData(data);
            return this;
        }

        public ResponseResult<T> build() {
            return responseResult;
        }
    }
}
