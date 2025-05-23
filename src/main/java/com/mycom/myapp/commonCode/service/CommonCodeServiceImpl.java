package com.mycom.myapp.commonCode.service;

import com.mycom.myapp.commonCode.dto.CommonCodeDto;
import com.mycom.myapp.commonCode.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeRepository codeRepository;

    @Override
    public List<CommonCodeDto> getCodesByGroupCode(String groupCode) {
        return codeRepository.findByGroupCode(groupCode)
                .stream()
                .map(code -> new CommonCodeDto(
                        code.getId().getCode(),
                        code.getCodeName(),
                        code.getOrderNo()
                ))
                .collect(Collectors.toList());
    }
}
