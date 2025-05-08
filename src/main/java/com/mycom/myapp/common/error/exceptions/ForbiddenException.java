package com.mycom.myapp.common.error.exceptions;

import com.mycom.myapp.common.enums.ResponseCode;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ResponseCode responseCode) {
        super(responseCode);
    }
}
