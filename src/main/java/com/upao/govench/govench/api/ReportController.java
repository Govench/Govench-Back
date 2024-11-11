package com.upao.govench.govench.api;

import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.ReportService;
import com.upao.govench.govench.service.PDFService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
public class ReportController {
    private final ReportService reportService;
    private final PDFService pdfService;
    private final UserService userService;

    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource> downloadUserReportPdf() {
        Integer userId= userService.getAuthenticatedUserIdFromJWT();
        ReportResponseDTO reportResponseDTO = reportService.generateReport(userId);
        ByteArrayInputStream pdfStream = pdfService.generateUserReportPdf(reportResponseDTO, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=user_report_" + userId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
