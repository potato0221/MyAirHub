package com.ll.myairhub.global.exceptions;

import com.ll.myairhub.global.rsData.RsData;
import com.ll.myairhub.standard.base.Empty;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final RsData<Empty> rsData;

    public GlobalException(String resultCode, String msg) {
        super("resultCode=" + resultCode + ",msg=" + msg);
        this.rsData = RsData.of(resultCode, msg);
    }

    public GlobalException() {
        this("400-0", "에러");
    }

    public GlobalException(String msg) {
        this("400-0", msg);
    }

}
