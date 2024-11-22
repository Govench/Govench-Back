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
            int newFollowers,
            int connectionsMade,
            double averageRating,
            List<ReportResponseDTO.EventStatsDTO> eventStatsDTOs) {

        ReportResponseDTO.ReportSummaryDTO summaryDTO = new ReportResponseDTO.ReportSummaryDTO();
        summaryDTO.setNewFollowers(newFollowers);
        summaryDTO.setConnectionsMade(connectionsMade);
        summaryDTO.setAverageRating(averageRating);

        summaryDTO.setEventStatsDTO(new ReportResponseDTO.EventStatsDTO());
        summaryDTO.getEventStatsDTO().setCreatedEvents(
                eventStatsDTOs.stream()
                        .flatMap(eventStats -> eventStats.getCreatedEvents().stream())
                        .toList()
        );

        ReportResponseDTO responseDTO = new ReportResponseDTO(
                "Reporte generado exitosamente",
                summaryDTO
        );
        return responseDTO;
    }

    public ReportResponseDTO.EventStatsDTO toEventStatsDTO(
            List<EventBasicDTO> createdEvents) {

        ReportResponseDTO.EventStatsDTO eventStatsDTO = new ReportResponseDTO.EventStatsDTO();
        eventStatsDTO.setCreatedEvents(createdEvents);
        return eventStatsDTO;
    }
}

