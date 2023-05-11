package com.project.capstone.Service.Impl;

import com.itextpdf.text.DocumentException;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.project.capstone.Models.UserFeed;
import com.project.capstone.Repositories.Service.UserFeedRepoService;
import com.project.capstone.Repositories.Service.UserRepositoryService;
import com.project.capstone.Service.CsvPdfService;
import com.project.capstone.Utils.GeneratePdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Service
public class CsvPdfServiceImpl implements CsvPdfService {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    UserFeedRepoService userFeedRepoService;

    @Override
    public ResponseEntity<?> csvFileUpload(MultipartFile file) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepositoryService.findByEmail(email).get().getEnabled()){
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CsvToBean<UserFeed> csvToBean = new CsvToBeanBuilder<UserFeed>(reader)
                    .withType(UserFeed.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<UserFeed> users = csvToBean.parse();
            userFeedRepoService.saveAll(users);
            return ResponseEntity.ok("File upload successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No access for this user.");
    }

    @Override
    public void exportCsv(HttpServletResponse response) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserFeed> myFeed = userFeedRepoService.findByEmail(email);
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + email + "\"");
        StatefulBeanToCsv<UserFeed> writer = new StatefulBeanToCsvBuilder<UserFeed>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();
        writer.write(myFeed);
    }

    @Override
    public ResponseEntity<InputStreamResource> exportPdf(HttpServletResponse response) throws DocumentException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\""+email+".Pdf\"";
        response.setHeader(headerKey,headerValue);

        List<UserFeed> userFeeds = userFeedRepoService.findByEmail(email);
        ByteArrayInputStream bis = GeneratePdf.exportPdf(userFeeds);

        return ResponseEntity
                .ok()
                .header(headerKey,headerValue)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
