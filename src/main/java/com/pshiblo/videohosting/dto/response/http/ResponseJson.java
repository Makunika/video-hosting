package com.pshiblo.videohosting.dto.response.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseJson {

    public static class Builder<T> {

        private T value;
        private boolean isSuccess;
        private String errorMessage;
        private HttpStatus httpStatus;

        protected Builder(boolean isSuccess) {
            this.isSuccess = isSuccess;
            this.httpStatus = HttpStatus.BAD_REQUEST;
            errorMessage = "";
        }

        public ResponseEntity<JsonEntity<T>> withValue(T value) {
            this.value = value;
            return build();
        }

        public ResponseEntity<JsonEntity<T>> withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return build();
        }

        public Builder<T> withHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResponseEntity<JsonEntity<T>> build() {
            if (isSuccess) {
                return new ResponseEntity<>(new JsonEntity<T>(isSuccess, value, errorMessage), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new JsonEntity<T>(isSuccess, null, errorMessage), httpStatus);
            }
        }


    }


    public static <T> Builder<T> success() {
        return new Builder<T>(true);
    }

    public static <T> Builder<T> error() {
        return new Builder<T>(false);
    }

}
