package org.bonn.se2.services.util;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class CryptoFunctions {

    public static String hash(String plaintext) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(plaintext);
    }

    public static boolean checkPw(String hash, String plain) {
        return hash.equals(plain);
    }

}
