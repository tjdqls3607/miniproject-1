package com.mycom.myapp.common.entity;

import com.mycom.myapp.common.enums.DataStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DataStatus status = DataStatus.ACTIVATED;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String etc;

    public void softDelete() {
        this.status = DataStatus.DEACTIVATED;
    }

    public void restore() {
        this.status = DataStatus.ACTIVATED;
    }

    @PrePersist
    public void prePersist() {
        if(status==null) status = DataStatus.ACTIVATED;
    }
}