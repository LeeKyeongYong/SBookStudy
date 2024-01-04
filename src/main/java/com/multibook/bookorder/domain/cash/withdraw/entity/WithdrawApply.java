package com.multibook.bookorder.domain.cash.withdraw.entity;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static lombok.AccessLevel.PROTECTED;
@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class WithdrawApply extends BaseTime {
    private LocalDateTime withdrawDate;
    private LocalDateTime cancelDate;
    @ManyToOne
    private Member applicant;
    private String bankName;
    private String bankAccountNo;
    private long cash;
    private String msg;

    public boolean isCancelDone() {
        return cancelDate != null;
    }

    public boolean isWithdrawDone() {
        return withdrawDate != null;
    }

    public void setWithdrawDone() {
        withdrawDate = LocalDateTime.now();
    }

    public String getForPrintWithdrawStatus() {
        if (withdrawDate != null)
            return "처리완료(" + withdrawDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (withdrawDate == null) return "-";

        return "처리가능";
    }

    public String getForPrintCancelStatus() {
        if (cancelDate != null)
            return "취소완료(" + cancelDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        return "-";
    }

    public void setCancelDone(String msg) {
        cancelDate = LocalDateTime.now();
        this.msg = msg;
    }
}