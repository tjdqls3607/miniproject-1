package com.mycom.myapp.commonCode.Entity;

import com.mycom.myapp.commonCode.Entity.CommonCodeGroup;
import com.mycom.myapp.commonCode.key.CommonCodeId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CommonCode {

    @EmbeddedId
    private CommonCodeId id;

    @ManyToOne
    @MapsId("notiCode") // 공통키 공유
    @JoinColumn(name = "noti_code")
    private CommonCodeGroup group;

    private String codeName;
    private int orderNo;
}
