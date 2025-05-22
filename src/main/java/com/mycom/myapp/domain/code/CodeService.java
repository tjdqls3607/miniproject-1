package com.mycom.myapp.domain.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mycom.myapp.common.entity.Code;
import com.mycom.myapp.common.entity.key.CodeKey;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeService {

	private final CodeRepository codeRepository;
	
	public CodeResultDto insertCode(Code code) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			codeRepository.save(code);
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto updateCode(Code code) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			codeRepository.save(code);
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto deleteCode(CodeKey codeKey) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			codeRepository.deleteById(codeKey);
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto listCode(String groupCode) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			List<Code> codeList = codeRepository.findByGroupCode(groupCode);
			List<CodeDto> codeDtoList = new ArrayList<>();
			
			codeList.forEach( code -> codeDtoList.add( CodeDto.fromCode(code)));
			codeResultDto.setCodeDtoList(codeDtoList);
			// count
			Long count = codeRepository.count();
			codeResultDto.setCount(count);
			
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		return codeResultDto;
	}
	
	public CodeResultDto detailCode(CodeKey codeKey) {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			Optional<Code> optionalcode = codeRepository.findById(codeKey);
			
			optionalcode.ifPresentOrElse(
				detailCode -> {
					codeResultDto.setCodeDto( CodeDto.fromCode(detailCode));
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
	
	public CodeResultDto countCode() {
		CodeResultDto codeResultDto = new CodeResultDto();
		try {
			Long count = codeRepository.count();
			codeResultDto.setCount(count);
			
			codeResultDto.setResult("success");
		} catch(Exception e) {
			e.printStackTrace();
			codeResultDto.setResult("fail");
		}
		
		return codeResultDto;
	}
}
