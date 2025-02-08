package org.example;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DecryptFile {
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH_BIT = 128;

    public static void decryptFile (SecretKey key, File inputFile, File outputFile) {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(inputFile);
            fileOutputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading input or output file", e);
        }
        byte [] iv = new byte [IV_SIZE];
        try {
            fileInputStream.read(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
            byte [] buffer = new byte [4096];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating cipher with AES", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Error generating cipher with padding", e);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException("Could not initialize cipher", e);
        }
    }
}
