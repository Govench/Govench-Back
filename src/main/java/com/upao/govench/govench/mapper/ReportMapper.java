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

        summaryDTO.getEventStatsDTO().setEventRatings(
                eventStatsDTOs.stream()
                        .flatMap(eventStats -> eventStats.getEventRatings().stream())
                        .toList()
        );

        // Usar el constructor con los argumentos requeridos
        ReportResponseDTO responseDTO = new ReportResponseDTO(
                "Reporte generado exitosamente",  // message
                summaryDTO,                       // summary
                "http://example.com/reporte.pdf"  // reportUrl
        );

        System.out.println("ReportResponseDTO generado: " + responseDTO);
        return responseDTO;
    }

    /**
     * Método para mapear las estadísticas simplificadas de eventos creados.
     */
   //

    /**
     * Método para mapear las estadísticas de calificación de eventos.
     */
    public ReportResponseDTO.EventRatingStatsDTO toEventRatingStatsDTO(
            Event event,
            double averageRating,
            int totalRatings) {

        System.out.println("Mapeando EventRatingStatsDTO para el evento ID: " + event.getId());
        ReportResponseDTO.EventRatingStatsDTO eventRatingStatsDTO = new ReportResponseDTO.EventRatingStatsDTO();
        eventRatingStatsDTO.setEventId(event.getId());
        eventRatingStatsDTO.setEventTitle(event.getTittle());
        eventRatingStatsDTO.setAverageRating(averageRating);
        eventRatingStatsDTO.setTotalRatings(totalRatings);
        System.out.println("EventRatingStatsDTO generado: " + eventRatingStatsDTO);
        return eventRatingStatsDTO;
    }

    /**
     * Método para construir las estadísticas de eventos combinando eventos creados y calificados.
     */
    public ReportResponseDTO.EventStatsDTO toEventStatsDTO(
            List<EventBasicDTO> createdEvents,
            List<ReportResponseDTO.EventRatingStatsDTO> eventRatings) {

        System.out.println("Construyendo EventStatsDTO...");
        ReportResponseDTO.EventStatsDTO eventStatsDTO = new ReportResponseDTO.EventStatsDTO();
        eventStatsDTO.setCreatedEvents(createdEvents);
        eventStatsDTO.setEventRatings(eventRatings);
        System.out.println("EventStatsDTO generado: " + eventStatsDTO);
        return eventStatsDTO;
    }
}

