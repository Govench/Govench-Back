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
    }
    @Data
    public static class CommunityStatsDTO {
        private int totalCommunities;
        private int totalUsersInCommunities;
        private int totalPostsInCommunities;
    }
}
