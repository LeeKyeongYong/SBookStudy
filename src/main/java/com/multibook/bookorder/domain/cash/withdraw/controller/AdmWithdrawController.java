package com.multibook.bookorder.domain.cash.withdraw.controller;

import com.multibook.bookorder.domain.cash.withdraw.entity.WithdrawApply;
import com.multibook.bookorder.domain.cash.withdraw.service.WithdrawService;
import com.multibook.bookorder.domain.global.exceptions.GlobalException;
import com.multibook.bookorder.global.rq.Rq;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/adm/withdraw")
@Controller
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
public class AdmWithdrawController {

    private final WithdrawService withdrawService;
    private final Rq rq;
    @GetMapping("applyList")
    public String showApplyList(){
        List<WithdrawApply> withdrawApplies = withdrawService.findAll();
        rq.attr("withdrawApplies",withdrawApplies);
        return "domain/cash/withdraw/adm/applyList";
    }

    //출금신청 취소
    @DeleteMapping("/{id}/cancel")
    public String cancel(@PathVariable long id){
        WithdrawApply withdrawApply = withdrawService.findById(id)
                .orElseThrow(()-> new GlobalException("400-1","출금 신청이 존재하지 않습니다."));

        if(!withdrawService.canCancel(rq.getMember(),withdrawApply))
            throw new GlobalException("403-2","출금 신청을 취소할 수 없습니다.");

        withdrawService.cancel(withdrawApply);

        return rq.redirect("/adm/withdraw/applyList","해당 출금 신청이 취소되었습니다.");
    }

    //출금신청 삭제
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id){
        WithdrawApply withdrawApply = withdrawService.findById(id)
                .orElseThrow(()-> new GlobalException("400-1","출금 신청이 존재하지 않습니다."));

        if(!withdrawService.canDelete(rq.getMember(),withdrawApply))
            throw new GlobalException("403-2","출금 신청을 삭제할 수 없습니다.");

        withdrawService.delete(withdrawApply);

        return rq.redirect("/adm/withdraw/applyList","하당 출금 신청이 삭제 되었습니다.");
    }

    //출금처리 완료
    @PostMapping("/{id}/do")
    public String withdraw(@PathVariable long id){

        WithdrawApply withdrawApply = withdrawService.findById(id)
                .orElseThrow(()->new GlobalException("400-1","출금 신청이 존재하지 않습니다."));

        if(!withdrawService.canWithdraw(rq.getMember(),withdrawApply))
            throw new GlobalException("403-2","출금 신청을 취소할 수 없습니다.");

        withdrawService.withdraw(withdrawApply);

        return rq.redirect("/adm/withdraw/applyList","출금처리가 완료되었습니다.");
    }

}
