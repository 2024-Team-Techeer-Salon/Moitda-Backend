package com.techeersalon.moitda.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    private LocalDateTime createAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updateAt = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private boolean isdeleted = Boolean.FALSE;

//    public void delete() {
//        this.is_deleted = Boolean.TRUE;
//    }
}