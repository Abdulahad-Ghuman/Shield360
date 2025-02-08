package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateHash {
    public static byte[] generateHash (File inputFile) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            return digest.digest(Files.readAllBytes(inputFile.toPath()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find hashing algorithm", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }
}
