package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.ReportMapper;
import com.upao.govench.govench.model.dto.EventBasicDTO;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.dto.UserBasicDTO;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.repository.*;
import com.upao.govench.govench.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private UserRepository userRepository;
    private UserEventRepository userEventRepository;
    private UserCommunityRepository userCommunityRepository;
    private ReportMapper reportMapper;
    private FollowRepository followRepository;
    private EventRepository eventRepository;
    private RatingEventRepository ratingEventRepository;
    private ParticipantRepository participantRepository;
    private OrganizerRepository organizerRepository;

    @Override
    public ReportResponseDTO generateReport(Integer userId) {
        System.out.println("Iniciando generación de reporte para el usuario con ID: " + userId);

        // Determinar el rol del usuario
        String role = userRepository.findRoleByUserId(userId);
        System.out.println("Rol del usuario: " + role);

        // Buscar los datos básicos según el rol
        UserBasicDTO userBasicDTO;
        if ("ROLE_PARTICIPANT".equalsIgnoreCase(role)) {
            userBasicDTO = participantRepository.findParticipantDtoByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Participant not found for userId: " + userId));
        } else if ("ROLE_ORGANIZER".equalsIgnoreCase(role)) {
            userBasicDTO = organizerRepository.findOrganizerDtoByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Organizer not found for userId: " + userId));
        } else {
            throw new RuntimeException("Unsupported role for userId: " + userId);
        }

        System.out.println("Datos básicos del usuario: " + userBasicDTO);

        // Continúa con la lógica del reporte
        int newFollowing = followRepository.countByFollowingId(userId);
        System.out.println("Número de nuevos seguidores: " + newFollowing);

        int connectionsMade = followRepository.countByFollowerId(userId);
        System.out.println("Conexiones realizadas: " + connectionsMade);

        int eventsAttended = userEventRepository.countDistinctEventsByUserId(userId);
        System.out.println("Eventos asistidos: " + eventsAttended);

        // Resto de la lógica
        List<EventBasicDTO> createdEvents = eventRepository.findSimplifiedEventsByOwnerId(userId);
        System.out.println("Eventos creados: " + createdEvents);

        List<ReportResponseDTO.EventRatingStatsDTO> eventRatings = eventRepository.findEventRatingsByOwnerId(userId);
        System.out.println("Calificaciones de eventos: " + eventRatings);

        ReportResponseDTO.EventStatsDTO eventStatsDTO = reportMapper.toEventStatsDTO(createdEvents, eventRatings);

        return reportMapper.toReportResponseDTO(
                newFollowing,
                connectionsMade,
                List.of(eventStatsDTO)
        );
    }

    private double calculateAverageRating(int eventId) {
        List<RatingEvent> ratingEvents = ratingEventRepository.findRatingsByEventId(eventId);
        System.out.println("Ratings encontrados para el evento ID " + eventId + ": " + ratingEvents);
        return ratingEvents.stream()
                .mapToInt(RatingEvent::getValorPuntuacion)
                .average()
                .orElse(0.0);
    }
}
