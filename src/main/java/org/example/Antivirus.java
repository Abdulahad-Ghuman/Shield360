/**
 * Calculates hashes of files in a directory and compares it to known malware hashes.
 *
 * @author Abdulahad Ghuman
 * @version 1.0
 * @since 2025-02-08
 */

package org.example;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Scanner;

public class Antivirus {

    /**
     * Hash Set that includes all virus hashes from file.
     */

    static HashSet <String> virusHashes = new HashSet <> ();

    /**
     * Scanner to read user input.
     */

    static Scanner scanner;

    /**
     * Reads through known malware hex values and imports them.
     */

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

    /**
     * Recursively looks through directory and compares hash of file to known malware hashes.
     *
     * @param directoryToScan The directory to scan.
     * @param secretKey The encryption key to encrypt files.
     */

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
