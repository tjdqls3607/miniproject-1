package com.mycom.myapp.domain.code;

import com.mycom.myapp.common.entity.Code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CodeDto {
	
	private String notiCode;
	private String code;
	private String codeName;
	private int orderNo;
	
	public static CodeDto fromCode(Code code) {
		return CodeDto.builder()
				.notiCode(code.getCodeKey().getNotiCode())
				.code(code.getCodeKey().getCode())
				.codeName(code.getCodeName())
				.orderNo(code.getOrderNo())
				.build();
	}
}
