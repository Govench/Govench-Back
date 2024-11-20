package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.ReportMapper;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.repository.*;
import com.upao.govench.govench.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public ReportResponseDTO generateReport(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calcular métricas generales
        List<UserEvent> userEvents = userEventRepository.findByUser(user);
        List<UserCommunity> userCommunities = userCommunityRepository.findByUser(user);
        int totalEvents = userEvents.size();
        int newFollowers = followRepository.findByFollowing(user).size();
        int connectionsMade = followRepository.findByFollower(user).size();
        int eventsAttended = (int) userEvents.stream().map(UserEvent::getEvent).distinct().count();
        int totalCommunities = (int) userCommunities.stream().map(UserCommunity::getCommunity).distinct().count();
        int totalUsersInCommunities = (int) userCommunities.stream().map(UserCommunity::getUser).distinct().count();
        int totalPostsInCommunities = userCommunities.stream()
                .flatMap(userCommunity -> userCommunity.getCommunity().getPost().stream())
                .distinct()
                .toList().size();

        // Crear DTO de estadísticas de comunidad
        ReportResponseDTO.CommunityStatsDTO communityStatsDTO = reportMapper.toCommunityStatsDTO(
                totalCommunities,
                totalUsersInCommunities,
                totalPostsInCommunities
        );

        // Procesar estadísticas de eventos
        List<ReportResponseDTO.SimplifiedEventDTO> createdEvents = eventRepository.findByOwner_Id(userId)
                .stream()
                .map(reportMapper::toSimplifiedEventDTO)
                .toList();

        List<ReportResponseDTO.EventRatingStatsDTO> eventRatings = eventRepository.findByOwner_Id(userId)
                .stream()
                .map(event -> {
                    double averageRating = calculateAverageRating(event.getId());
                    int totalRatings = ratingEventRepository.countByEventId_IdAndValorPuntuacion(event.getId(), 5);
                    return reportMapper.toEventRatingStatsDTO(event, averageRating, totalRatings);
                })
                .toList();

        ReportResponseDTO.EventStatsDTO eventStatsDTO = reportMapper.toEventStatsDTO(createdEvents, eventRatings);

        // Crear y devolver el reporte
        return reportMapper.toReportResponseDTO(
                totalEvents,
                newFollowers,
                connectionsMade,
                eventsAttended,
                communityStatsDTO,
                List.of(eventStatsDTO)
        );
    }

    private double calculateAverageRating(int eventId) {
        List<RatingEvent> ratingEvents = ratingEventRepository.findRatingsByEventId(eventId);
        return ratingEvents.stream()
                .mapToInt(RatingEvent::getValorPuntuacion)
                .average()
                .orElse(0.0);
    }
}
