package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.Participant;
import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportMapper {

    public ReportResponseDTO toReportResponseDTO(int totalEvents, int newFollowers, int connectionsMade,
                                                 int eventsAttended, ReportResponseDTO.CommunityStatsDTO communityStatsDTO) {
        ReportResponseDTO responseDTO = new ReportResponseDTO();
        ReportResponseDTO.ReportSummaryDTO summaryDTO = new ReportResponseDTO.ReportSummaryDTO();

        summaryDTO.setTotalEvents(totalEvents);
        summaryDTO.setNewFollowers(newFollowers);
        summaryDTO.setConnectionsMade(connectionsMade);
        summaryDTO.setEventsAttended(eventsAttended);
        summaryDTO.setCommunityStats(communityStatsDTO);

        responseDTO.setSummary(summaryDTO);

        return responseDTO;
    }
    public ReportResponseDTO.CommunityStatsDTO toCommunityStatsDTO(int totalCommunities, int totalUsersInCommunities, int totalPostsInCommunities) {
        ReportResponseDTO.CommunityStatsDTO communityStatsDTO = new ReportResponseDTO.CommunityStatsDTO();
        communityStatsDTO.setTotalCommunities(totalCommunities);
        communityStatsDTO.setTotalUsersInCommunities(totalUsersInCommunities);
        communityStatsDTO.setTotalPostsInCommunities(totalPostsInCommunities);
        return communityStatsDTO;
    }
}

