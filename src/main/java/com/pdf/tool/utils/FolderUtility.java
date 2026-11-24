package com.pdf.tool.utils;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderUtility {
    /**
     * Removes empty files and directories within the directory identified by {@code folder},
     * including the root if it becomes empty.
     *
     * @throws IOException if the folder cannot be accessed or an entry cannot be removed
     */
    public static void removeEmptySubfolders(String folder) throws IOException {
        removeEmptySubfolders(Paths.get(folder));
    }

    /**
     * Removes zero-byte regular files and empty directories within {@code folder},
     * including the root if it becomes empty. Symbolic links are preserved.
     *
     * @throws IOException if the folder cannot be accessed or an entry cannot be removed
     */
    public static void removeEmptySubfolders(Path folder) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(
                folder, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        if (!attributes.isDirectory()) {
            throw new NotDirectoryException(folder.toString());
        }

        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                if (attributes.isRegularFile() && attributes.size() == 0) {
                    System.out.println(file.toString());
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path directory, IOException exception) throws IOException {
                if (exception != null) {
                    throw exception;
                }
                try {
                    Files.delete(directory);
                } catch (DirectoryNotEmptyException ignored) {
                    // Non-empty directories must be kept.
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }



}
