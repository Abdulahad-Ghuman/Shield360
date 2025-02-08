package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Scanner;

public class MonitorDirectory {
    static Scanner scanner = new Scanner(System.in);
    public static void watchDirectory () {
        System.out.println("Enter directory to monitor for changes");
        Path path = Paths.get(scanner.nextLine());
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            while (true) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = watchEvent.kind();
                    Path filePath = path.resolve((Path) watchEvent.context());
                    if (Files.exists(filePath)) {
                        System.out.print("Hash before change: " + Arrays.toString(GenerateHash.generateHash(filePath.toFile())));
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.print(" for file that was created at " + filePath);
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.print(" for a file that was modified at " + filePath);
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println(" for a file that was modified at " + filePath);
                    }
                    if (Files.exists(filePath)) {
                        System.out.println("Hash after change: " + Arrays.toString(GenerateHash.generateHash(filePath.toFile())));
                    }
                }
                if (!watchKey.reset()) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create watch service or register path", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not create Watch Key", e);
        }
    }
}
