/**
 * Encrypts file.
 *
 * @author Abdulahad Ghuman
 * @version 1.0
 * @since 2025-02-08
 */

package org.example;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptFile {

    /**
     * Key size.
     */

    private static final int AES_KEY_SIZE = 256;

    /**
     * IV size.
     */

    private static final int IV_SIZE = 12;

    /**
     * Tag length.
     */

    private static final int TAG_LENGTH_BIT = 128;

    /**
     * Generates new AES key.
     * @return Encryption key.
     */

    public static SecretKey generateAESKey () {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }

    /**
     * Encrypts the file.
     *
     * @param key Key used to encrypt file.
     * @param inputFile FIle to encrypt.
     * @param outputFile Encrypted file.
     */

    public static void encryptFile (SecretKey key, File inputFile, File outputFile) {
        Cipher cipher;
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        SecureRandom secureRandom = new SecureRandom();
        byte [] iv = new byte [IV_SIZE];
        secureRandom.nextBytes(iv);
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating cipher with AES", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Error generating cipher with padding", e);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException("Could not initialize cipher", e);
        }
        try {
            fileInputStream = new FileInputStream(inputFile);
            fileOutputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error finding input or output file", e);
        }
        try {
            fileOutputStream.write(iv);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write IV", e);
        }
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
        byte [] buffer = new byte [4096];
        int bytesRead;
        try {
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot write encrypted file", e);
        }
        try {
            fileInputStream.close();
            fileOutputStream.close();
            cipherOutputStream.close();
        } catch (IOException e) {
            System.out.println("Could not close resources");
        }
    }
}
