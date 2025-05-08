package com.mycom.myapp.common;
import com.mycom.myapp.common.enums.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Slf4j
public class ResponseDTO<T> {
    private String code;
    private String message;
    private T data;
    private PagedMetaDTO meta;

    public ResponseDTO(String code, String message, T o, PagedMetaDTO o1) {
        this.code = code;
        this.message = message;
        this.data = o;
        this.meta = o1;
    }

    public static <T> ResponseDTO<T> success(ResponseCode code) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), null, null);
    }

    // ✅ 성공 응답 (메타 없음)
    public static <T> ResponseDTO<T> success(ResponseCode code, T data) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), data, null);
    }

    // ✅ 성공 응답 (메타 포함)
    public static <T> ResponseDTO<T> success(ResponseCode code, T data, PagedMetaDTO meta) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), data, meta);
    }

    public static <T> ResponseDTO<List<T>> success(ResponseCode code, List<T> dataList) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), dataList, null);
    }

    public static <T> ResponseDTO<List<T>> success(ResponseCode code, List<T> dataList, PagedMetaDTO meta) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), dataList, meta);
    }

    // ✅ 에러 응답
    public static ResponseDTO<Void> error(ResponseCode code) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), null, null);
    }

    public static <T> ResponseDTO<T> error(ResponseCode code, T data) {
        return new ResponseDTO<>(code.getCode(), code.getMessage(), data, null);
    }


    public static ResponseDTO<Void> error(String code, String message) {
        return new ResponseDTO<>(code, message, null, null);
    }
}
