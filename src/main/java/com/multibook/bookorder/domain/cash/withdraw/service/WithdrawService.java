package com.multibook.bookorder.domain.cash.withdraw.service;

import com.multibook.bookorder.domain.cash.cash.entity.CashLog;
import com.multibook.bookorder.domain.cash.withdraw.entity.WithdrawApply;
import com.multibook.bookorder.domain.cash.withdraw.repository.WithdrawApplyRepository;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawService {
    private final WithdrawApplyRepository withdrawApplyRepository;
    private final MemberService memberService;

    public boolean canApply(Member actor, long cash) {
        return actor.getRestCash() >= cash;
    }

    @Transactional
    public void apply(Member applicant, long cash, String bankName, String bankAccountNo) {
        WithdrawApply apply = WithdrawApply.builder()
                .applicant(applicant)
                .cash(cash)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .build();

        withdrawApplyRepository.save(apply);
    }

    public List<WithdrawApply> findByApplicant(Member applicant) {
        return withdrawApplyRepository.findByApplicantOrderByIdDesc(applicant);
    }

    public Optional<WithdrawApply> findById(long id) {
        return withdrawApplyRepository.findById(id);
    }

    public boolean canDelete(Member actor, WithdrawApply withdrawApply) {
        if (withdrawApply.isWithdrawDone()) return false;
        if (withdrawApply.isCancelDone()) return false;

        if (actor.isAdmin()) return true;

        if (!withdrawApply.getApplicant().equals(actor)) return false;

        return true;
    }

    @Transactional
    public void delete(WithdrawApply withdrawApply) {
        withdrawApplyRepository.delete(withdrawApply);
    }

    public List<WithdrawApply> findAll() {
        return withdrawApplyRepository.findAllByOrderByIdDesc();
    }

    public boolean canCancel(Member actor, WithdrawApply withdrawApply) {
        if (withdrawApply.isWithdrawDone()) return false;
        if (withdrawApply.isCancelDone()) return false;

        if (!actor.isAdmin()) return false;

        if (withdrawApply.getApplicant().getRestCash() >= withdrawApply.getCash()) return false;

        return true;
    }

    public boolean canWithdraw(Member actor, WithdrawApply withdrawApply) {
        if (withdrawApply.isWithdrawDone()) return false;
        if (withdrawApply.isCancelDone()) return false;

        if (!actor.isAdmin()) return false;

        if (withdrawApply.getApplicant().getRestCash() < withdrawApply.getCash()) return false;

        return true;
    }

    @Transactional
    public void withdraw(WithdrawApply withdrawApply) {
        withdrawApply.setWithdrawDone();

        memberService.addCash(withdrawApply.getApplicant(), withdrawApply.getCash() * -1, CashLog.EvenType.출금__통장입금, withdrawApply);
    }

    @Transactional
    public void cancel(WithdrawApply withdrawApply) {
        withdrawApply.setCancelDone("관리자에 의해 취소됨, 잔액부족");
    }
}