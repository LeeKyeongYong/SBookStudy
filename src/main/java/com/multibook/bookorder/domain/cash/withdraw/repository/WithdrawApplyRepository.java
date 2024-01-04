package com.multibook.bookorder.domain.cash.withdraw.repository;

import com.multibook.bookorder.domain.cash.withdraw.entity.WithdrawApply;
import com.multibook.bookorder.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawApplyRepository extends JpaRepository<WithdrawApply, Long> {
    List<WithdrawApply> findByApplicantOrderByIdDesc(Member applicant);

    List<WithdrawApply> findAllByOrderByIdDesc();
}