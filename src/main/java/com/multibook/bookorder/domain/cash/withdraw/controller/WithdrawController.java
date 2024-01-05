package com.multibook.bookorder.domain.cash.withdraw.controller;

import com.multibook.bookorder.domain.cash.withdraw.entity.WithdrawApply;
import com.multibook.bookorder.domain.cash.withdraw.service.WithdrawService;
import com.multibook.bookorder.domain.global.exceptions.GlobalException;
import com.multibook.bookorder.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/withdraw")
@Controller
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
public class WithdrawController {
    private final WithdrawService withdrawService;
    private final Rq rq;

    @GetMapping("applyList")
    public String showApplyList(){
        List<WithdrawApply> withdrawApplies = withdrawService.findByApplicant(rq.getMember());
        rq.attr("withdrawApplies",withdrawApplies);

        return "domain/cach/withdrawapplyList";
    }

    @GetMapping("/apply")
    public String showApply(){
        return "domain/cash/withdraw/apply";
    }

    public record ApplyForm(@NotNull long cash,
                            @NotBlank String bankName,@NotBlank String bankAccountNo)
    {}

    @PostMapping("/apply")
    public String apply(@Valid ApplyForm applyForm){
        if(!withdrawService.canApply(rq.getMember(),applyForm.cash))
            throw new GlobalException("400-1","출금 신청이 불가능합니다.");
        withdrawService.apply(rq.getMember(),applyForm.cash,applyForm.bankName, applyForm.bankAccountNo);
        
        return rq.redirect("/withdraw/applyList","출금 신청이 완료되었습니다.");
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id){
        WithdrawApply withdrawApply = withdrawService.findById(id)
                .orElseThrow(()->new GlobalException("400-1","출금 신청이 존재하지 않습니다."));

        if(!withdrawService.canDelete(rq.getMember(),withdrawApply))
            throw new GlobalException("403-2","출금 신청을 취소할 수 없습니다.");

        withdrawService.delete(withdrawApply);

        return rq.redirect("/withdraw/applyList","해당 출금 신청이 삭제되었습니다.");
    }

}
