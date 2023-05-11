package com.project.capstone.Api;

import com.itextpdf.text.DocumentException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("/csv_pdf")
public interface CsvPdfApi {

    @PostMapping("/csv/upload")
    public ResponseEntity<?> csvFileUpload(@RequestParam("file") MultipartFile file) throws IOException;

    @GetMapping("/exportCsv")
    public void exportCsv(HttpServletResponse response) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;

    @GetMapping("/exportPdf")
    public ResponseEntity<InputStreamResource> exportPdf(HttpServletResponse response) throws DocumentException;

}
