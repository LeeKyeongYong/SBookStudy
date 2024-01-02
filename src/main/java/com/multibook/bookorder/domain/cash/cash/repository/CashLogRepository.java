package com.multibook.bookorder.domain.cash.cash.repository;

import com.multibook.bookorder.domain.cash.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog,Long> {
}
