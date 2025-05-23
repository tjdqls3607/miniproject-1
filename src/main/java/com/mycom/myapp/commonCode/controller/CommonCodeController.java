package com.mycom.myapp.commonCode.controller;

import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.entity.Code;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.commonCode.dto.CommonCodeDto;
import com.mycom.myapp.commonCode.service.CommonCodeService;
import com.mycom.myapp.domain.code.CodeDto;
import com.mycom.myapp.domain.code.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-code")
public class CommonCodeController {

    private final CodeService codeService;

    private final CommonCodeService commonCodeService;

    /**
     * 특정 코드 그룹(noticode)에 해당하는 코드 목록 반환
     * 예: /api/common-code?groupCode=010
     */
    // ✅ 일반 코드 목록 (DTO: CommonCodeDto)
    @GetMapping("/list")
    public List<CommonCodeDto> getCodesByGroupCode(@RequestParam String groupCode) {
        return commonCodeService.getCodesByGroupCode(groupCode);
    }

    // ✅ 공통 코드 조회 (DTO: CodeDto + ResponseDTO)


    @GetMapping("/api/common-code/options")
    public List<CommonCodeDto> getGameOptionCodes() {
        return commonCodeService.getCodesByGroupCode("050"); // 예: "풋살 옵션"
    }


    @GetMapping
    public ResponseDTO<List<CodeDto>> getByGroupCode(@RequestParam("groupCode") String groupCode) {
        List<Code> codes = codeService.getCodesByGroupCode(groupCode);
        List<CodeDto> result = codes.stream()
                .map(CodeDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseDTO.success(ResponseCode.SUCCESS,result);
    }

    // ✅ 프론트에서 사용할 경기 옵션 전용
    @GetMapping("/options")
    public ResponseDTO<List<CodeDto>> getGameOptions() {
        List<Code> codes = codeService.getCodesByGroupCode("050");
        List<CodeDto> result = codes.stream()
                .map(CodeDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseDTO.success(ResponseCode.SUCCESS,result);
    }
}
