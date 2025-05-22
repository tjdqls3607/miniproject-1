//package com.mycom.myapp.commonCode.Entity;
//
//import com.mycom.myapp.commonCode.key.CommonCodeId;
//import jakarta.persistence.EmbeddedId;
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//
//@Entity
//public class CommonCode {
//    @EmbeddedId
//    private CommonCodeId id;
//
//    @ManyToOne
//    @JoinColumn(name = "noti_code", insertable = false, updatable = false)
//    private CommonCodeGroup group;
//
//    private String codeName;
//    private int orderNo;
//
//}
