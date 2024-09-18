package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/user/{userId}")
    public ReportResponseDTO generateUserReport(@PathVariable Integer userId) {
        return reportService.generateReport(userId);
    }
}
