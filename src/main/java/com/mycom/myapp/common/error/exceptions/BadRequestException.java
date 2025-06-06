package com.mycom.myapp.common.error.exceptions;

import com.mycom.myapp.common.enums.ResponseCode;

public class BadRequestException extends BaseException {
    public BadRequestException(ResponseCode responseCode) {
        super(responseCode);
    }

    public BadRequestException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}