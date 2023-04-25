package com.noldaga.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnsApplicationException extends RuntimeException{

    private ErrorCode errorCode; //errorCode 도 기본적으로 메세지가 있긴함
    private String message; //errorCode 메세지보다 좀더 자세한 ...

    public SnsApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage(){
        if(message==null){
            return errorCode.getMessage();
        }
        return String.format("%s. %s", errorCode.getMessage(), message);
    }

}
