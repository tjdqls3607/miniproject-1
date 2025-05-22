//package com.mycom.myapp.commonCode.service;
//
//import com.mycom.myapp.commonCode.Entity.CommonCode;
//import com.mycom.myapp.commonCode.dto.CommonCodeDto;
//import com.mycom.myapp.domain.code.CodeRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//public class CommonCodeServiceImpl  implements CommonCodeService{
//
//    private final CodeRepository codeRepository;
//
//    @Override
//    public List<CommonCodeDto> getCodesByGroupCode(String groupCode) {
//        return codeRepository.findByGroupCode(groupCode)
//                .stream()
//                .map(code -> new CommonCodeDto(
//                        code.getCodekey().getCode(),
//                        code.getName(),
//                        code.getOrderNo()
//                ))
//        .collect(Collectors.toList());
//    }
//}
