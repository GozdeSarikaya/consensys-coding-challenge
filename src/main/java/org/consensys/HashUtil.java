package org.consensys;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    private static final MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public static byte[] hash(byte[] input) {
        return digest.digest(input);
    }

    public static byte[] hash(byte[] left, byte[] right) {
        digest.update(left);
        digest.update(right);
        return digest.digest();
    }
}
