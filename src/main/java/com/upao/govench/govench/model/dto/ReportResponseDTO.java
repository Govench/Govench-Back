package com.upao.govench.govench.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReportResponseDTO {
    private String message;
    private ReportSummaryDTO summary;
    private String reportUrl;

    @Data
    public static class ReportSummaryDTO {
        private int totalEvents;
        private int newFollowers;
        private int connectionsMade;
        private int eventsAttended;
        private int participationPercentage;
        private CommunityStatsDTO communityStats;
        private EventStatsDTO eventStatsDTO;
    }
    @Data
    public static class CommunityStatsDTO {
        private int totalCommunities;
        private int totalUsersInCommunities;
        private int totalPostsInCommunities;
    }

    @Data
    public static class EventStatsDTO {
        private List<SimplifiedEventDTO> createdEvents;
        private List<EventRatingStatsDTO> eventRatings;
    }

    @Data
    public static class SimplifiedEventDTO {
        private int id;
        private String tittle;
        private LocalDate date;
    }

    @Data
    public static class EventRatingStatsDTO {
        private int eventId;
        private String eventTitle;
        private double averageRating;
        private int totalRatings;
    }
}
