package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.ReportResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    ReportResponseDTO generateReport(Integer userId);
}
