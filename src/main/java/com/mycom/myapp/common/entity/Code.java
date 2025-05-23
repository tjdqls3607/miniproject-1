package com.mycom.myapp.common.entity;

import com.mycom.myapp.common.entity.key.CodeKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Code {

    @EmbeddedId
    CodeKey codeKey;

    @Column(name="code_name")
    private String codeName;

    @Column(name="order_no")
    private int orderNo;

    public String getCode() {
        return codeKey.getCode();  // 내부 키에서 꺼내옴
    }

}