package com.mycom.myapp.domain.code;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.common.entity.Code;
import com.mycom.myapp.common.entity.key.CodeKey;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CodeController {

	private final CodeService codeService;
	
	// 페이징된 목록
	@GetMapping("/codes")
	public CodeResultDto listCode(@RequestParam("groupCode") String groupCode) {
		return codeService.listCode(groupCode);
	}
	
	// 상세
	@GetMapping("/codes/{groupCode}/{code}")
	public CodeResultDto detailCode(@PathVariable("groupCode") String groupCode, @PathVariable("code") String code) {
		CodeKey codeKey = new CodeKey(groupCode, code);
		return codeService.detailCode(codeKey);
	}
	
    // 등록
    @PostMapping("/codes")
    public CodeResultDto insertCode(
        @RequestParam("groupCode") String groupCode,
        @RequestParam("code") String code,
        @RequestParam("codeName") String codeName,
        @RequestParam("orderNo") int orderNo
    ) {
        CodeKey codeKey = new CodeKey(groupCode, code);
        Code codeEntity = new Code();
        codeEntity.setCodeKey(codeKey);
        codeEntity.setCodeName(codeName);
        codeEntity.setOrderNo(orderNo);
        return codeService.insertCode(codeEntity);
    }
    
	// 수정
	@PutMapping("/codes")
	public CodeResultDto updateCode(		
		@RequestParam("groupCode") String groupCode,
		@RequestParam("code") String code,
		Code codeParam
	) {
		CodeKey codeKey = new CodeKey(groupCode, code);
		codeParam.setCodeKey(codeKey);
		return codeService.updateCode(codeParam);
	}
	
	// 삭제
	@DeleteMapping("/codes/{groupCode}/{code}")
	public CodeResultDto deleteCode(@PathVariable("groupCode") String groupCode, @PathVariable("code") String code) {
		CodeKey codeKey = new CodeKey(groupCode, code);
		return codeService.deleteCode(codeKey);
	}
	
	// count
	@GetMapping("/codes/count")
	public CodeResultDto countCode() {
		return codeService.countCode();
	}
}
