package com.mycom.myapp.common.entity.key;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CodeKey implements Serializable{
    private static final long serialVersionUID = 1L;

    private String notiCode;
    private String code;

    public CodeKey() { }
    public CodeKey(String notiCode, String code) {
        this.notiCode = notiCode;
        this.code = code;
    }
}