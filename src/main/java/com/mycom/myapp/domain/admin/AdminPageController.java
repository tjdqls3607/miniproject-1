package com.mycom.myapp.domain.admin;

import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.domain.code.CodeDto;
import com.mycom.myapp.domain.code.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/common-code")
@PreAuthorize("hasRole('ADMIN')")   // 관리자로 접근 제한
public class AdminPageController {

    private final CodeService codeService;

    // 게임옵션 추가
    @PostMapping
    public ResponseDTO<Void> addOption(@RequestBody CodeDto dto) {
        codeService.addCode(dto.getNotiCode(),dto.getCode(), dto.getCodeName());
        return ResponseDTO.success(ResponseCode.CREATED);
    }

    // 게임옵션 삭제
    @DeleteMapping("/{groupCode}/{code}")
    public ResponseDTO<Void> deleteOption(@PathVariable String groupCode, @PathVariable String code) {
        codeService.deleteCode(groupCode, code);
        return ResponseDTO.success(ResponseCode.SUCCESS);
    }
}