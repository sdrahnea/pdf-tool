package com.pdf.tool.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
@EnableScheduling
public class PdfDecryptor {

    @Value("${app.decrypt.destination.file}")
    private String FILE_NUM_01;

    @Value("${app.decrypt.password}")
    private String PASSWORD;

    public static void main(String... args) throws IOException {
        SpringApplication.run(PdfDecryptor.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            log.info("FILE_NUM_01: {}", FILE_NUM_01);

            log.info("Start to decrypt file ...");

            File file = new File(FILE_NUM_01);
            PDDocument pdd = PDDocument.load(file, PASSWORD);
            pdd.setAllSecurityToBeRemoved(true);
            pdd.save(file);
            pdd.close();

            log.info("Decryption task has been completed!");

        };
    }

}
