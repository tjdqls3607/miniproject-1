package com.mycom.myapp.domain.code;

import java.util.List;

import com.mycom.myapp.domain.groupCode.NotiGroupCodeDto;

import lombok.Data;

@Data
public class CodeResultDto {

    private String result;
    private CodeDto codeDto;
    private NotiGroupCodeDto groupCodeDto;
    private List<CodeDto> codeDtoList;
    private List<NotiGroupCodeDto> groupCodeDtoList;
    private long count;
}