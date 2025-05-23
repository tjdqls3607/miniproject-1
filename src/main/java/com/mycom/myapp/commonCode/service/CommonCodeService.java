package com.mycom.myapp.commonCode.service;

import com.mycom.myapp.commonCode.dto.CommonCodeDto;

import java.util.List;

public interface CommonCodeService {
    List<CommonCodeDto> getCodesByGroupCode(String groupCode);
}
