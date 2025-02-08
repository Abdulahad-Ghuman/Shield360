package org.example;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class KeyManager {
    private static final String KEYSTORE_FILE = "keystore.jks";
    private static KeyStore keyStore;
    private static Console console = System.console();
    public static void loadKeyStore () {
        try {
            keyStore = KeyStore.getInstance("JCEKS");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Could not create key store", e);
        }
        File file = new File(KEYSTORE_FILE);
        try {
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(KEYSTORE_FILE);
                System.out.println("Enter password for key store to access it.");
                keyStore.load(fileInputStream, console.readPassword());
            } else {
                System.out.println("Enter password for key store and remember it.");
                System.out.println("This password will not be saved for security concerns.");
                keyStore.load(null, console.readPassword());
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not open key store", e);
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Could not load store key", e);
        }
    }
    public static boolean hasKey (String key) {
        try {
            return keyStore.containsAlias(key);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Cannot find key store", e);
        }
    }
    public static void saveKey (String key, SecretKey secretKey) {
        KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(secretKey);
        System.out.println("Enter password for key entry and remember it.");
        System.out.println("This password will not be saved for security concerns.");
        char [] password = console.readPassword()
        KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(password);
        try {
            keyStore.setEntry(key, keyEntry, param);
            keyStore.store(new FileOutputStream(KEYSTORE_FILE), password);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Could not store key", e);
        } catch (NoSuchAlgorithmException | IOException | CertificateException e) {
            throw new RuntimeException("Could not save key", e);
        }
    }
    public static SecretKey loadKey (String key) {
        try {
            FileInputStream fileInputStream = new FileInputStream(KEYSTORE_FILE);
            System.out.println("Enter password for key store to access it.");
            keyStore.load(fileInputStream, console.readPassword());
            System.out.println("Enter password for key to access it.");
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(key, new KeyStore.PasswordProtection(console.readPassword()))).getSecretKey();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not open key store", e);
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Could not load key store", e);
        } catch (UnrecoverableEntryException | KeyStoreException e) {
            throw new RuntimeException("Could not access key", e);
        }
    }
    public static SecretKey createOrLoadKey () {
        loadKeyStore();
        SecretKey secretKey;
        if (hasKey("EncryptionKey")) {
            secretKey = loadKey("EncryptionKey");
        } else {
            secretKey = EncryptFile.generateAESKey();
            saveKey("EncryptionKey", secretKey);
        }
        return secretKey;
    }
}
