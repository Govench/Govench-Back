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
public class ReportController {
    private final ReportService reportService;
    private final PDFService pdfService;
    private final UserService userService;

    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource> downloadUserReportPdf() {
        System.out.println("Iniciando generación de reporte PDF...");

        // Obtener el ID del usuario autenticado
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        System.out.println("Usuario autenticado con ID: " + userId);

        // Generar el DTO del reporte
        ReportResponseDTO reportResponseDTO = reportService.generateReport(userId);
        System.out.println("ReportResponseDTO generado: " + reportResponseDTO);

        // Generar el PDF
        ByteArrayInputStream pdfStream = pdfService.generateUserReportPdf(reportResponseDTO, userId);
        if (pdfStream == null) {
            System.out.println("Error al generar el PDF, flujo de datos nulo.");
            return ResponseEntity.internalServerError().build();
        }

        System.out.println("PDF generado exitosamente para el usuario ID: " + userId);

        // Configurar encabezados de respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=user_report_" + userId + ".pdf");

        System.out.println("Preparando respuesta HTTP con contenido PDF...");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
