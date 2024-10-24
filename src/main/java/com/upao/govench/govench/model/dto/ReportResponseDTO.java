package com.upao.govench.govench.model.dto;

import lombok.Data;

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
        private EventRatingStatsDTO eventRatingStats;
        private UserActivityStatsDTO userActivityStats;
    }
    @Data
    public static class CommunityStatsDTO {
        private int totalCommunities;
        private int totalUsersInCommunities;
        private int totalPostsInCommunities;
    }

    @Data
    public static class EventRatingStatsDTO {
        private int totalRatingsGiven;
        private double averageEventRating;
    }

    @Data
    public static class UserActivityStatsDTO {
        private int totalPostsByUser;
        private int totalCommentsByUser;
    }
}
