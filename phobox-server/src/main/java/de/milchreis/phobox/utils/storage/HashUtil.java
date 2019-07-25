package de.milchreis.phobox.utils.storage;

import de.milchreis.phobox.exceptions.HashGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HashUtil {


    public static String md5File(File file) throws HashGenerationException {
        try {
            return getHash(Files.readAllBytes(file.toPath()), "MD5");
        } catch (IOException e) {
            throw new HashGenerationException("Could not generate hash from file", e);
        }
    }

    public static String sha1File(File file) throws HashGenerationException {
        try {
            return getHash(Files.readAllBytes(file.toPath()), "SHA-1");
        } catch (IOException e) {
            throw new HashGenerationException("Could not generate hash from file", e);
        }
    }

    public static String md5(String content) {
        return getHash(content.getBytes(), "MD5");
    }

    public static String sha1(String content) {
        return getHash(content.getBytes(), "SHA-1");
    }

    private static String getHash(byte[] content, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(content);
            return DatatypeConverter.printHexBinary(digest).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            log.error("Could nit create MD5 instance", e);
        }
        return null;
    }

}
