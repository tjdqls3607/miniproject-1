package com.mycom.myapp.common.error.exceptions;

import com.mycom.myapp.common.enums.ResponseCode;
import java.util.Arrays;

public class InternalServerException extends BaseException{
    public InternalServerException(ResponseCode responseCode) {
        super(responseCode);
    }

    public InternalServerException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

    public InternalServerException(ResponseCode responseCode, Throwable cause) {
        super(responseCode, cause);
    }
}
