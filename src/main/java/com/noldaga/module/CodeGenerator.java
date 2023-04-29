package com.noldaga.module;


import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeGenerator {

    private final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMBERS = "0123456789";
    private final String ALL = UPPER + LOWER + NUMBERS;

    public String generateRandomCode(int size) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(ALL.length());
            sb.append(ALL.charAt(index));
        }
        return sb.toString();
    }
}
