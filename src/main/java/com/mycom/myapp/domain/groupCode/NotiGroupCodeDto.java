package com.mycom.myapp.domain.groupCode;

import com.mycom.myapp.common.entity.NotiGroupCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotiGroupCodeDto {

    private String notiCode;
    private String notiCodeName;
    private String notiCodeDesc;

    public static NotiGroupCodeDto fromGroupcode(NotiGroupCode notiGroupCode) {
        return NotiGroupCodeDto.builder()
                .notiCode(notiGroupCode.getNotiCode())
                .notiCodeName(notiGroupCode.getNotiCodeName())
                .notiCodeDesc(notiGroupCode.getNotiCodeDesc())
                .build();
    }
}