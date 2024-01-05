package com.multibook.bookorder.global.jpa;

import com.multibook.bookorder.util.UtZip;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    public String getModelName() {
        return UtZip.str.lcfirst(this.getClass().getSimpleName());
    }
}