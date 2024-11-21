package com.upao.govench.govench.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ReportResponseDTO {
    private String message;
    private ReportSummaryDTO summary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportSummaryDTO {
        private int newFollowers;
        private int connectionsMade;
        private EventStatsDTO eventStatsDTO;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventStatsDTO {
        private List<EventBasicDTO> createdEvents;
    }

}
