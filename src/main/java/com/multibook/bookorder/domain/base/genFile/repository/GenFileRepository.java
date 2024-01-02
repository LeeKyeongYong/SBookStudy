package com.multibook.bookorder.domain.base.genFile.repository;

import com.multibook.bookorder.global.jpa.BaseEntity;
import com.multibook.bookorder.util.UtZip;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //@CreatedDate,@LastModifiedDate를 사용하기 위해 필요
@Getter
@ToString(callSuper = true)
public abstract class GenFileRepository extends BaseEntity {
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    public String getModelName(){
        return UtZip.str.lcfirst(this.getClass().getSimpleName());
    }
}
