package org.example;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Scanner;

public class Antivirus {
    static HashSet <String> virusHashes = new HashSet <> ();
    static Scanner scanner;

    public static void getHashes () {
        try {
            scanner = new Scanner(new File("Malware.txt"));
            while (scanner.hasNextLine()) {
                virusHashes.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find malware text file", e);
        }
    }
    public static void scanForMalware (File directoryToScan, SecretKey secretKey) {
        if (directoryToScan != null) {
            for (File entry : directoryToScan.listFiles()) {
                if (entry.isDirectory()) {
                    scanForMalware(entry, secretKey);
                } else {
                    String hex = HexFormat.of().formatHex(GenerateHash.generateHash(entry));
                    if (virusHashes.contains(hex)) {
                        System.out.println(entry.getAbsolutePath() + " is a virus and will be quarantined.");
                        EncryptFile.encryptFile(secretKey, entry, entry);
                    }
                }
            }
        } else {
            System.out.println("Could not find directory");
        }
    }
}
