package com.mycom.myapp.commonCode.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CommonCodeGroup {
    @Id
    private String notiCode;

    private String notiCodeName;
    private String notiCodeDesc;

    @OneToMany(mappedBy = "group")
    private List<CommonCode> codes;
}
