package com.upao.govench.govench.service;

import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GraphService {
    byte[] generateWeeklyPostChart(Integer userId);
    byte[] generateMonthlyPostChart(Integer userId);
    byte[] generateUserStarChart(Integer userId);
    List<byte[]> generateEventStarCharts(Integer userId);
    byte[] generateEventParticipantsChart(Integer userId);
}
