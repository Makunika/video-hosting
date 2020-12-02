package com.pshiblo.videohosting.dto.response.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseJson {

    public static class Builder<T> {

        private T value;
        private boolean isSuccess;
        private String errorMessage;

        protected Builder(boolean isSuccess) {
            this.isSuccess = isSuccess;
            errorMessage = "";
        }

        public Builder<T> withValue(T value) {
            this.value = value;
            return this;
        }

        public Builder<T> withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public ResponseEntity<JsonEntity<T>> build() {
            if (isSuccess) {
                return new ResponseEntity<>(new JsonEntity<T>(isSuccess, value, errorMessage), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new JsonEntity<T>(isSuccess, null, errorMessage), HttpStatus.BAD_REQUEST);
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
