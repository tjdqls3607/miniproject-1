package com.mycom.myapp.commonCode.repository;

import com.mycom.myapp.commonCode.Entity.CommonCode;
import com.mycom.myapp.commonCode.key.CommonCodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, CommonCodeId> {

    @Query("SELECT c FROM CommonCode c WHERE c.id.notiCode = :groupCode ORDER BY c.orderNo")
    List<CommonCode> findByGroupCode(@Param("groupCode") String groupCode);
}
