package de.milchreis.phobox.utils.storage;

import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper {

    /**
     * Returns a MD5 hash for a given input string. (hex encoded)
     *
     * @param input expects an input string
     * @return hex encoded md5 string of the input
     */
    public static String getMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);

        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static String getMD5(File file) throws IOException {
        try (InputStream is = Files.newInputStream(file.toPath())) {
            return DigestUtils.md5DigestAsHex(is);
        }
    }
}
