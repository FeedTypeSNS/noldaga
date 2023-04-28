package com.noldaga.module;


import com.noldaga.domain.CodeDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CodeValidator {
    //todo 시간 제한을 두어서 코드가 사라지게끔 해야함 -> redis에서 시간제한 해서 사용하면될듯
    private final Map<Integer, String> storage = new ConcurrentHashMap<>();
    private final AtomicInteger keyGenerator = new AtomicInteger(0);

    private final int codeSize=4;


    public void validateCode(Integer codeId, String codeRequest) {
        String code = storage.get(codeId);
        if (code == null || !code.equals(codeRequest)) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE);
        }
        storage.remove(codeId);
    }

    public CodeDto generateCode() {
        String code = createRandomCode(codeSize);
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code);
        return CodeDto.of(key, code);
    }

    private String createRandomCode(int size) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while (buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        return buffer.toString();
//        return "1234";
    }


}
