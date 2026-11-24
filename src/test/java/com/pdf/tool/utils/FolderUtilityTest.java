package com.pdf.tool.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Assumptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FolderUtilityTest {
    @TempDir
    Path root;

    @Test
    void removesNestedEmptyFoldersAndKeepsFilesAndRoot() throws IOException {
        Path emptyTree = Files.createDirectories(root.resolve("empty").resolve("nested"));
        Path nonEmpty = Files.createDirectories(root.resolve("kept").resolve("nested"));
        Path file = Files.createFile(nonEmpty.resolve("document.pdf"));
        Path emptySibling = Files.createDirectory(root.resolve("kept").resolve("empty"));

        FolderUtility.removeEmptySubfolders(root);

        assertFalse(Files.exists(emptyTree));
        assertFalse(Files.exists(root.resolve("empty")));
        assertFalse(Files.exists(emptySibling));
        assertTrue(Files.exists(file));
        assertTrue(Files.isDirectory(root));
    }

    @Test
    void removesAnEmptyRoot() throws IOException {
        Path emptyRoot = Files.createDirectory(root.resolve("empty-root"));
        FolderUtility.removeEmptySubfolders(emptyRoot);

        assertFalse(Files.exists(emptyRoot));
    }

    @Test
    void acceptsStringFolderPath() throws IOException {
        Path empty = Files.createDirectories(root.resolve("empty").resolve("nested"));
        Path file = Files.createFile(root.resolve("file"));

        FolderUtility.removeEmptySubfolders(root.toString());

        assertFalse(Files.exists(empty));
        assertFalse(Files.exists(root.resolve("empty")));
        assertTrue(Files.exists(file));
        assertTrue(Files.isDirectory(root));
        assertThrows(NoSuchFileException.class,
                () -> FolderUtility.removeEmptySubfolders(root.resolve("missing").toString()));

        Path emptyRoot = Files.createDirectories(root.resolve("empty-root").resolve("nested")).getParent();
        FolderUtility.removeEmptySubfolders(emptyRoot.toString());
        assertFalse(Files.exists(emptyRoot));
    }

    @Test
    void rejectsMissingAndNonDirectoryRoots() throws IOException {
        Path missing = root.resolve("missing");
        Path file = Files.createFile(root.resolve("file"));

        assertThrows(NoSuchFileException.class, () -> FolderUtility.removeEmptySubfolders(missing));
        assertThrows(NotDirectoryException.class, () -> FolderUtility.removeEmptySubfolders(file));
    }

    @Test
    void doesNotFollowSymbolicLinks() throws IOException {
        Path scan = Files.createDirectory(root.resolve("scan"));
        Path target = Files.createDirectories(root.resolve("target").resolve("child"));
        Path link = scan.resolve("link");
        try {
            Files.createSymbolicLink(link, root.resolve("target"));
        } catch (UnsupportedOperationException | SecurityException | IOException exception) {
            Assumptions.assumeTrue(false, "Symbolic links are unavailable: " + exception);
        }

        assertThrows(NotDirectoryException.class, () -> FolderUtility.removeEmptySubfolders(link));
        FolderUtility.removeEmptySubfolders(scan);

        assertTrue(Files.exists(link, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.isDirectory(target));
    }
}
