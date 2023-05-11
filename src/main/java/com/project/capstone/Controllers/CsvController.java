package com.project.capstone.Controllers;

import com.itextpdf.text.DocumentException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.project.capstone.Api.CsvPdfApi;
import com.project.capstone.Service.CsvPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CsvController implements CsvPdfApi {

    @Autowired
    CsvPdfService csvPdfService;

    @Override
    public ResponseEntity<?> csvFileUpload(MultipartFile file) throws IOException {
        return csvPdfService.csvFileUpload(file);
    }

    @Override
    public void exportCsv(HttpServletResponse response) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        csvPdfService.exportCsv(response);
    }

    @Override
    public ResponseEntity<InputStreamResource> exportPdf(HttpServletResponse response) throws DocumentException {
        return csvPdfService.exportPdf(response);
    }

}
