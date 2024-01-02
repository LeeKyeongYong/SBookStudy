package com.multibook.bookorder.domain.base.genFile.repository;

import com.multibook.bookorder.domain.base.genFile.entity.GenFile;
import com.multibook.bookorder.global.jpa.BaseEntity;
import com.multibook.bookorder.util.UtZip;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface GenFileRepository extends JpaRepository<GenFile, Long> {
    List<GenFile> findByRelTypeCodeAndRelIdOrderByTypeCodeAscType2CodeAscFileNoAsc(String relTypeCode, Long relId);

    Optional<GenFile> findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeAndFileNo(String relTypeCode, long relId, String typeCode, String type2Code, long fileNo);

    List<GenFile> findByRelTypeCodeAndRelIdInOrderByTypeCodeAscType2CodeAscFileNoAsc(String relTypeCode, long[] relIds);

    List<GenFile> findByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeOrderByFileNoAsc(String relTypeCode, long relId, String typeCode, String type2Code);

    List<GenFile> findByRelTypeCodeAndRelId(String relTypeCode, long relId);

    Optional<GenFile> findTop1ByRelTypeCodeAndRelIdAndTypeCodeAndType2CodeOrderByFileNoDesc(String relTypeCode, Long relId, String typeCode, String type2Code);

    List<GenFile> findByRelTypeCodeAndTypeCodeAndType2Code(String relTypeCode, String typeCode, String type2Code);

    List<GenFile> findByRelTypeCode(String relTypeCode);

    List<GenFile> findByRelTypeCodeAndCreateDateBefore(String relTypeCode, LocalDateTime dateTime);
}