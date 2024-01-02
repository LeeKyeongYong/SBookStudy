package com.multibook.bookorder.domain.global.exceptions;

import com.multibook.bookorder.global.rsData.RsData;
import lombok.Getter;

public class GlobalException extends RuntimeException {
    @Getter
    private RsData<?> rsData;

    public GlobalException(String resultCode, String msg) {
        super(resultCode + ": " + msg);

        rsData = RsData.of(resultCode, msg);
    }
}