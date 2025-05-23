package com.mycom.myapp.commonCode.key;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CommonCodeId implements Serializable {
    private String notiCode;
    private String code;
}
