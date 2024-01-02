package com.multibook.bookorder.global.rsData;


import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@Getter
public class RsData<T> {
    private final String resultCode;
    private final String msg;
    private final T data;
    private final int statusCode;

    public static <T> RsData<T> of(String resultCode, String msg, T data){
        int statusCode = Integer.parseInt(resultCode.split("-",2)[0]);

        return new RsData<>(resultCode,msg,data,statusCode);
    }

    public static <T> RsData<T> of(String resultCode,String msg){
        return of(resultCode,msg,null);
    }

    public boolean isSuccess(){
        return statusCode >= 200 && statusCode < 400;
    }

    public boolean isFail(){
        return !isSuccess();
    }

    public <T> RsData<T> of(T data){
        return RsData.of(resultCode,msg,data);
    }

}
