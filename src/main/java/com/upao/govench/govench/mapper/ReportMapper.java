package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.Participant;
import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportMapper {

    public ReportResponseDTO toReportResponseDTO(User user, int totalEvents, int newFollowers, int connectionsMade,
                                                 int eventsAttended, int totalCommunities, int totalUsersInCommunities,
                                                 int totalPostsInCommunities) {
        ReportResponseDTO responseDTO = new ReportResponseDTO();
        ReportResponseDTO.ReportSummaryDTO summaryDTO = new ReportResponseDTO.ReportSummaryDTO();
        ReportResponseDTO.CommunityStatsDTO communityStatsDTO = new ReportResponseDTO.CommunityStatsDTO();

        summaryDTO.setTotalEvents(totalEvents);
        summaryDTO.setNewFollowers(newFollowers);
        summaryDTO.setConnectionsMade(connectionsMade);
        summaryDTO.setEventsAttended(eventsAttended);

        communityStatsDTO.setTotalCommunities(totalCommunities);
        communityStatsDTO.setTotalUsersInCommunities(totalUsersInCommunities);
        communityStatsDTO.setTotalPostsInCommunities(totalPostsInCommunities);

        summaryDTO.setCommunityStats(communityStatsDTO);

        responseDTO.setMessage("Reporte generado exitosamente.");
        responseDTO.setSummary(summaryDTO);
        responseDTO.setReportUrl("/reports/user_" + user.getId() + "_actividad.pdf");

        return responseDTO;
    }

}

