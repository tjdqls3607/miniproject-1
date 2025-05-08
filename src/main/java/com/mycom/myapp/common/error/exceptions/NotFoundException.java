package com.mycom.myapp.common.error.exceptions;

import com.mycom.myapp.common.enums.ResponseCode;

public class NotFoundException extends BaseException {
    public NotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
    public NotFoundException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
