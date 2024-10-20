package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.ReportResponseDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public interface PDFService {
    ByteArrayInputStream generateUserReportPdf(ReportResponseDTO reportResponseDTO);
}
