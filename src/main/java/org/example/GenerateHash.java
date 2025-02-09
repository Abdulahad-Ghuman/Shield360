/**
 * Generates MD5 hash from file.
 *
 * @author Abdulahad Ghuman
 * @version 1.0
 * @since 2025-02-08
 */

package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateHash {

    /**
     * Generates MD5 hash.
     *
     * @param inputFile File to generate hash.
     * @return byte array of hash.
     */

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
