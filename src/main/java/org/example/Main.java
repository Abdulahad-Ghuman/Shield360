/**
 * Shield 360 is a comprehensive security project that offers multiple tools.
 * This includes a file monitoring system with hashes.
 * Additionally, it involves an antivirus that uses encryption based quarantine.
 * Finally, it can simply encrypt/decrypt files.
 *
 * @author Abdulahad Ghuman
 * @version 1.0
 * @since 2025-02-08
 */

package org.example;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.Scanner;

public class Main {

    /**
     * Main method which provides a CLI to interact with application.
     */

    public static void main (String [] args) {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        SecretKey secretKey = KeyManager.createOrLoadKey();
        while (true) {
            System.out.println("Enter a number associated with one of the options");
            System.out.println("1: Scan a directory for viruses");
            System.out.println("2: Monitor a directory for changes");
            System.out.println("3: Enter a file to encrypt or decrypt");
            System.out.println("4: Exit the program.");
            result = scanner.nextLine();
            if (result.equals("1")) {
                Antivirus.getHashes();
                System.out.println("Enter directory to scan");
                Antivirus.scanForMalware(new File(scanner.nextLine()), secretKey);
            } else if (result.equals("2")) {
                MonitorDirectory.watchDirectory();
            } else if (result.equals("3")) {
                System.out.println("Enter a number associated with one of the options");
                System.out.println("1. Encrypt a file");
                System.out.println("2. Decrypt a file");
                String menuResult = scanner.nextLine();
                String inputFile;
                System.out.println("Enter location of file");
                inputFile = scanner.nextLine();
                String outputFile;
                if (menuResult.equals("1")) {
                    System.out.println("Enter location to save encrypted file");
                    outputFile = scanner.nextLine();
                    EncryptFile.encryptFile(secretKey, new File(inputFile), new File(outputFile));
                } else if (menuResult.equals("2")) {
                    System.out.println("Enter location to save decrypted file");
                    outputFile = scanner.nextLine();
                    DecryptFile.decryptFile(secretKey, new File(inputFile), new File(outputFile));
                } else {
                    System.out.println("Invalid option");
                }
            } else if (result.equals("4")) {
                scanner.close();
                System.exit(0);
            } else {
                System.out.println("Invalid option");
            }
        }
    }
}
