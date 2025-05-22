package com.mycom.myapp.domain.groupCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mycom.myapp.common.entity.NotiGroupCode;
import com.mycom.myapp.domain.code.CodeResultDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotiGroupCodeService {

	private final NotiGroupCodeRepository notiGroupCodeRepository;
	
	public CodeResultDto insertGroupCode(NotiGroupCode notiGroupCode) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			// isNew true
			notiGroupCode.setNew(true);
			notiGroupCodeRepository.save(notiGroupCode);
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto updateGroupCode(NotiGroupCode notiGroupCode) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			// isNew false
			notiGroupCode.setNew(false);
			notiGroupCodeRepository.save(notiGroupCode);
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto deleteGroupCode(String notiCode) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			notiGroupCodeRepository.deleteById(notiCode);
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto listGroupCode() {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			List<NotiGroupCode> groupCodeList = notiGroupCodeRepository.findAll();
			List<NotiGroupCodeDto> groupCodeDtoList = new ArrayList<>();
			
			groupCodeList.forEach( notiCode -> groupCodeDtoList.add( NotiGroupCodeDto.fromGroupcode(notiCode)));
			codeResultDto.setGroupCodeDtoList(groupCodeDtoList);
			// count
			Long count = notiGroupCodeRepository.count();
			codeResultDto.setCount(count);
			
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto detailGroupCode(String notiCode) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			Optional<NotiGroupCode> optionalGroupCode = notiGroupCodeRepository.findById(notiCode);
			
			optionalGroupCode.ifPresentOrElse(
				detailGroupCode -> {
					codeResultDto.setGroupCodeDto( NotiGroupCodeDto.fromGroupcode(detailGroupCode));
					codeResultDto.setResult("success");
				}, 
				() -> codeResultDto.setResult("fail")
			);
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto countGroupCode() {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			Long count = notiGroupCodeRepository.count();
			codeResultDto.setCount(count);
			
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		
		return codeResultDto;
	}
}
