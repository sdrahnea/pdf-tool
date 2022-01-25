package com.pdf.tool.service;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.IOException;

public class PdfMerger {

    private final static String DESTINATION_FILE = "";
    private final static String FILE_NUM_01 = "";
    private final static String FILE_NUM_02 = "";
    private final static String FILE_NUM_03 = "";

    public static void main(String... arg) throws IOException {
        PDFMergerUtility ut = new PDFMergerUtility();
        ut.addSource(FILE_NUM_01);
        ut.addSource(FILE_NUM_02);
        ut.addSource(FILE_NUM_03);

        ut.setDestinationFileName(DESTINATION_FILE);
        ut.mergeDocuments();
    }

}
