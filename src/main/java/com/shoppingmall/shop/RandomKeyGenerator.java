package com.shoppingmall.shop;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomKeyGenerator {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);


        String base64EncodedKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated Key: " + base64EncodedKey);
    }
}
