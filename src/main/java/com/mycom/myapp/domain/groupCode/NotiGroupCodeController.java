package com.mycom.myapp.domain.groupCode;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.common.entity.NotiGroupCode;
import com.mycom.myapp.domain.code.CodeResultDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotiGroupCodeController {

	private final NotiGroupCodeService notiGroupCodeService;
	
	// 페이징된 목록
	@GetMapping("/groupcodes")
	public CodeResultDto listGroupCode() {
		return notiGroupCodeService.listGroupCode();
	}
	
	// 상세
	@GetMapping("/groupcodes/{groupCode}")
	public CodeResultDto listGroupCode(@PathVariable("groupCode") String groupCode) {
		return notiGroupCodeService.detailGroupCode(groupCode);
	}
	
	// 등록
	@PostMapping("/groupcodes")
	public CodeResultDto insertGroupCode(NotiGroupCode notiGroupCode) {
		return notiGroupCodeService.insertGroupCode(notiGroupCode);
	}
	
	// 수정
	@PutMapping("/groupcodes")
	public CodeResultDto updateGroupCode(NotiGroupCode notiGroupCode) {
		return notiGroupCodeService.updateGroupCode(notiGroupCode);
	}
	
	// 삭제
	@DeleteMapping("/groupcodes/{groupCode}")
	public CodeResultDto deleteGroupCode(@PathVariable("groupCode") String groupCode) {
		return notiGroupCodeService.deleteGroupCode(groupCode);
	}
	
	// count
	@GetMapping("/groupcodes/count")
	public CodeResultDto countGroupCode() {
		return notiGroupCodeService.countGroupCode();
	}
}
