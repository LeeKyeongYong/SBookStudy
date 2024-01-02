package com.multibook.bookorder.global.jpa;

import com.multibook.bookorder.util.UtZip;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //@CreateDate, @LastModifiedDate를 사용하기 위해 필요.
@Getter
@ToString(callSuper = true)
public class BaseTime extends BaseEntity{
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    public String getModeName(){
        return UtZip.str.lcfirst(this.getClass().getSimpleName());
    }
}
