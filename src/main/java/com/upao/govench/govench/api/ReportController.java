package com.upao.govench.govench.api;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.ReportService;
import com.upao.govench.govench.service.PDFService;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final PDFService pdfService;

    @GetMapping("/user/{userId}")
    public ReportResponseDTO generateUserReport(@PathVariable Integer userId) {
        return reportService.generateReport(userId);
    }

    @GetMapping("/user/{userId}/pdf")
    public ResponseEntity<InputStreamResource> downloadUserReportPdf(@PathVariable Integer userId) {
        ReportResponseDTO reportResponseDTO = reportService.generateReport(userId);
        ByteArrayInputStream pdfStream = pdfService.generateUserReportPdf(reportResponseDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=user_report_" + userId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
