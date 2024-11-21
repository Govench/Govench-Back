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

    /**
     * Método principal para construir el DTO de respuesta del reporte.
     */
    public ReportResponseDTO toReportResponseDTO(
            int newFollowers,
            int connectionsMade,
            List<ReportResponseDTO.EventStatsDTO> eventStatsDTOs) {

        System.out.println("Construyendo ReportResponseDTO...");
        ReportResponseDTO.ReportSummaryDTO summaryDTO = new ReportResponseDTO.ReportSummaryDTO();
        summaryDTO.setNewFollowers(newFollowers);
        summaryDTO.setConnectionsMade(connectionsMade);

        System.out.println("Mapeando estadísticas de eventos...");
        summaryDTO.setEventStatsDTO(new ReportResponseDTO.EventStatsDTO());
        summaryDTO.getEventStatsDTO().setCreatedEvents(
                eventStatsDTOs.stream()
                        .flatMap(eventStats -> eventStats.getCreatedEvents().stream())
                        .toList()
        );

        // Usar el constructor con los argumentos requeridos
        ReportResponseDTO responseDTO = new ReportResponseDTO(
                "Reporte generado exitosamente",  // message
                summaryDTO
        );

        System.out.println("ReportResponseDTO generado: " + responseDTO);
        return responseDTO;
    }

    /**
     * Método para mapear las estadísticas simplificadas de eventos creados.
     */
   //

    /**
     * Método para construir las estadísticas de eventos combinando eventos creados y calificados.
     */
    public ReportResponseDTO.EventStatsDTO toEventStatsDTO(
            List<EventBasicDTO> createdEvents) {

        System.out.println("Construyendo EventStatsDTO...");
        ReportResponseDTO.EventStatsDTO eventStatsDTO = new ReportResponseDTO.EventStatsDTO();
        eventStatsDTO.setCreatedEvents(createdEvents);
        System.out.println("EventStatsDTO generado: " + eventStatsDTO);
        return eventStatsDTO;
    }
}

