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

    private ReportMapper reportMapper;
    private FollowRepository followRepository;
    private EventRepository eventRepository;
    private RatingEventRepository ratingEventRepository;

    @Override
    public ReportResponseDTO generateReport(Integer userId) {

        int newFollowing = followRepository.countByFollowingId(userId);
        int connectionsMade = followRepository.countByFollowerId(userId);

        List<EventBasicDTO> createdEvents = eventRepository.findSimplifiedEventsByOwnerId(userId);

        double averageRating = ratingEventRepository.findAverageRatingByUserId(userId).orElse(0.0);

        // **Paso 6: Combinar datos en un `EventStatsDTO`**
        ReportResponseDTO.EventStatsDTO eventStatsDTO = reportMapper.toEventStatsDTO(createdEvents);

        return reportMapper.toReportResponseDTO(
                newFollowing,
                connectionsMade,
                averageRating,
                List.of(eventStatsDTO)
        );
    }
}
