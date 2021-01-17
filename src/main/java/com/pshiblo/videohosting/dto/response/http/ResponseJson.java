package com.pshiblo.videohosting.dto.response.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseJson {

    public static class ErrorBuilder<T> {

        private String errorMessage;
        private HttpStatus httpStatus;

        protected ErrorBuilder() {
            this.httpStatus = HttpStatus.BAD_REQUEST;
            errorMessage = "";
        }

        public ResponseEntity<JsonEntity<T>> withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return build();
        }

        public ErrorBuilder<T> withHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResponseEntity<JsonEntity<T>> build() {
            return new ResponseEntity<>(new JsonEntity<T>(false, null, errorMessage), httpStatus);
        }
    }

    public static class SuccessBuilder<T> {

        private T value;

        protected SuccessBuilder() {
        }

        public ResponseEntity<JsonEntity<T>> withValue(T value) {
            this.value = value;
            return build();
        }

        public ResponseEntity<JsonEntity<T>> build() {
            return new ResponseEntity<>(new JsonEntity<T>(true, value, ""), HttpStatus.OK);
        }
    }


    public static <T> SuccessBuilder<T> success() {
        return new SuccessBuilder<T>();
    }

    public static <T> ErrorBuilder<T> error() {
        return new ErrorBuilder<T>();
    }

}
