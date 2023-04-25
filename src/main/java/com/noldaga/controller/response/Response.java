package com.noldaga.controller.response;


import lombok.Getter;

@Getter //스프링이 Json 으로 변환해줄때 필요
public class Response<T> {
    private String resultCode;
    private T result;

    private Response(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS", result);
    }

    public static Response<Void> success(){
        return Response.success(null);
    }

    public static Response<Void> error(String errorCode){
        return new Response<>(errorCode, null);
    }

    //토큰필터에서 에러발생시 에러결과 포맷팅 하기위해 : EntryPoint 에서 사용
    public String toStream() {
        if(result==null){
            return "{" +
                    "\"resultCode\":" + "\"" + resultCode + "\"," +
                    "\"result\":" + null +
                    "}";
        }
        return "{" +
                "\"resultCode\":" + "\"" + resultCode + "\"," +
                "\"result\":" + result + "\"" +
                "}";

    }
}
