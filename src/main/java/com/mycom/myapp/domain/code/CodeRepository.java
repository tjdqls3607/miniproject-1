package com.mycom.myapp.domain.code;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mycom.myapp.common.entity.Code;
import com.mycom.myapp.common.entity.key.CodeKey;

public interface CodeRepository extends JpaRepository<Code, CodeKey>{

    @Query("select c from Code c where c.id.notiCode = :notiCode order by c.orderNo")
    List<Code> findByGroupCode(@Param("notiCode") String groupCode);
}