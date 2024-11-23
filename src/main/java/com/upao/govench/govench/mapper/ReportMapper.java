package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.EventBasicDTO;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ReportMapper {

    public ReportResponseDTO toReportResponseDTO(
            List<EventBasicDTO> createdEvents) {
        return new ReportResponseDTO(
                "Reporte generado exitosamente",
                createdEvents
        );
    }
}

