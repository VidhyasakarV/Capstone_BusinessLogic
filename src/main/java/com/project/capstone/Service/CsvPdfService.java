package com.project.capstone.Service;

import com.itextpdf.text.DocumentException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CsvPdfService {

    public ResponseEntity<?> csvFileUpload(@RequestParam("file") MultipartFile file) throws IOException;

    public void exportCsv(HttpServletResponse response) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;

    public ResponseEntity<InputStreamResource> exportPdf(HttpServletResponse response) throws DocumentException;
}
