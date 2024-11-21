package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.ReportMapper;
import com.upao.govench.govench.model.dto.EventBasicDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
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

        String role = userRepository.findRoleByUserId(userId);
        System.out.println("Rol del usuario: " + role);

        // **Paso 2: Obtener datos básicos del usuario según su rol**
        UserBasicDTO userBasicDTO = getUserBasicDTOByRole(userId, role);
        System.out.println("Datos básicos del usuario: " + userBasicDTO);

        // **Paso 3: Obtener métricas**
        int newFollowing = followRepository.countByFollowingId(userId);
        int connectionsMade = followRepository.countByFollowerId(userId);
        int eventsAttended = userEventRepository.countDistinctEventsByUserId(userId);

        System.out.println("Nuevos seguidores: " + newFollowing);
        System.out.println("Conexiones realizadas: " + connectionsMade);
        System.out.println("Eventos asistidos: " + eventsAttended);

        // **Paso 4: Obtener eventos creados**
        List<EventBasicDTO> createdEvents = eventRepository.findSimplifiedEventsByOwnerId(userId);
        System.out.println("Eventos creados: " + createdEvents);

        // **Paso 5: Obtener calificaciones de eventos**
        List<RatingEventResponseDTO> eventRatings = ratingEventRepository.findRatingsWithEventTitleByEventId(userId);
        System.out.println("Calificaciones de eventos: " + eventRatings);

        // **Paso 6: Combinar datos en un `EventStatsDTO`**
        ReportResponseDTO.EventStatsDTO eventStatsDTO = reportMapper.toEventStatsDTO(createdEvents);

        // **Paso 7: Construir el `ReportResponseDTO`**
        return reportMapper.toReportResponseDTO(
                newFollowing,
                connectionsMade,
                List.of(eventStatsDTO)
        );
    }
    private UserBasicDTO getUserBasicDTOByRole(Integer userId, String role) {
        if ("ROLE_PARTICIPANT".equalsIgnoreCase(role)) {
            return participantRepository.findParticipantDtoByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Participant not found for userId: " + userId));
        } else if ("ROLE_ORGANIZER".equalsIgnoreCase(role)) {
            return organizerRepository.findOrganizerDtoByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Organizer not found for userId: " + userId));
        } else {
            throw new RuntimeException("Unsupported role for userId: " + userId);
        }
    }
}
