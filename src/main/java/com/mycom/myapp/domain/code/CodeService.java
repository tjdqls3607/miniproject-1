package com.mycom.myapp.domain.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycom.myapp.common.error.exceptions.BadRequestException;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mycom.myapp.common.entity.Code;
import com.mycom.myapp.common.entity.key.CodeKey;
import com.mycom.myapp.common.enums.ResponseCode;

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

    public List<Code> getCodesByGroupCode(String groupCode) {
        return codeRepository.findByGroupCode(groupCode);
    }

    public String getCodeName(String groupCode, String code) {
        CodeKey key = new CodeKey(groupCode, code);
        Code codeEntity = codeRepository.findById(key)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_COMMON_CODE));
        return codeEntity.getCodeName();
    }

    public void deleteCode(String groupCode, String code) {
        CodeKey key = new CodeKey(groupCode, code);
        codeRepository.deleteById(key);
    }

    public void addCode(String groupCode, String code, String codeName) {
        CodeKey key = new CodeKey(groupCode, code);

        // 중복 검사
        if (codeRepository.existsById(key)) {
            throw new BadRequestException(ResponseCode.INVALID_REQUEST, "이미 존재하는 코드입니다.");
        }

        Code entity = new Code();
        entity.setCodeKey(key);
        entity.setCodeName(codeName);
        entity.setOrderNo(0); // 기본값 또는 정렬용 우선순위. 필요 시 수정

        codeRepository.save(entity);
    }

    // 오버로드 버전도 제공 (DTO 기반)
    public void addCode(CodeDto dto) {
        addCode(dto.getNotiCode(), dto.getCode(), dto.getCodeName());
    }


}