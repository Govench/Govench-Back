package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.Participant;
import com.upao.govench.govench.model.entity.User;
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
            int totalEvents,
            int newFollowers,
            int connectionsMade,
            int eventsAttended,
            ReportResponseDTO.CommunityStatsDTO communityStatsDTO,
            List<ReportResponseDTO.EventStatsDTO> eventStatsDTOs) {

        ReportResponseDTO responseDTO = new ReportResponseDTO();
        ReportResponseDTO.ReportSummaryDTO summaryDTO = new ReportResponseDTO.ReportSummaryDTO();

        // Mapear datos al resumen
        summaryDTO.setTotalEvents(totalEvents);
        summaryDTO.setNewFollowers(newFollowers);
        summaryDTO.setConnectionsMade(connectionsMade);
        summaryDTO.setEventsAttended(eventsAttended);
        summaryDTO.setCommunityStats(communityStatsDTO);

        // Establecer estadísticas de eventos
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

        // Asignar el resumen al reporte principal
        responseDTO.setSummary(summaryDTO);
        return responseDTO;
    }

    /**
     * Método para mapear las estadísticas de comunidad.
     */
    public ReportResponseDTO.CommunityStatsDTO toCommunityStatsDTO(
            int totalCommunities,
            int totalUsersInCommunities,
            int totalPostsInCommunities) {

        ReportResponseDTO.CommunityStatsDTO communityStatsDTO = new ReportResponseDTO.CommunityStatsDTO();
        communityStatsDTO.setTotalCommunities(totalCommunities);
        communityStatsDTO.setTotalUsersInCommunities(totalUsersInCommunities);
        communityStatsDTO.setTotalPostsInCommunities(totalPostsInCommunities);
        return communityStatsDTO;
    }

    /**
     * Método para mapear las estadísticas simplificadas de eventos creados.
     */
    public ReportResponseDTO.SimplifiedEventDTO toSimplifiedEventDTO(Event event) {
        ReportResponseDTO.SimplifiedEventDTO simplifiedEventDTO = new ReportResponseDTO.SimplifiedEventDTO();
        simplifiedEventDTO.setId(event.getId());
        simplifiedEventDTO.setTittle(event.getTittle());
        simplifiedEventDTO.setDate(event.getDate());
        return simplifiedEventDTO;
    }

    /**
     * Método para mapear las estadísticas de calificación de eventos.
     */
    public ReportResponseDTO.EventRatingStatsDTO toEventRatingStatsDTO(
            Event event,
            double averageRating,
            int totalRatings) {

        ReportResponseDTO.EventRatingStatsDTO eventRatingStatsDTO = new ReportResponseDTO.EventRatingStatsDTO();
        eventRatingStatsDTO.setEventId(event.getId());
        eventRatingStatsDTO.setEventTitle(event.getTittle());
        eventRatingStatsDTO.setAverageRating(averageRating);
        eventRatingStatsDTO.setTotalRatings(totalRatings);
        return eventRatingStatsDTO;
    }

    /**
     * Método para construir las estadísticas de eventos combinando eventos creados y calificados.
     */
    public ReportResponseDTO.EventStatsDTO toEventStatsDTO(
            List<ReportResponseDTO.SimplifiedEventDTO> createdEvents,
            List<ReportResponseDTO.EventRatingStatsDTO> eventRatings) {

        ReportResponseDTO.EventStatsDTO eventStatsDTO = new ReportResponseDTO.EventStatsDTO();
        eventStatsDTO.setCreatedEvents(createdEvents);
        eventStatsDTO.setEventRatings(eventRatings);
        return eventStatsDTO;
    }
}

