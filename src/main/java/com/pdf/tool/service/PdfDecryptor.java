package com.pdf.tool.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

@Slf4j
public class PdfDecryptor {

    private final static String FILE_NUM_01 = "";
    private final static String PASSWORD = "";

    public static void main(String... arg) throws IOException {
        log.info("Start to decrypt file ...");

        File file = new File(FILE_NUM_01);
        PDDocument pdd = PDDocument.load(file, PASSWORD);
        pdd.setAllSecurityToBeRemoved(true);
        pdd.save(file);
        pdd.close();

        log.info("Decryption task has been completed!");
    }

}
