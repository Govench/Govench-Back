package com.upao.govench.govench.service;

import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GraphService {
    byte[] generateWeeklyPostChart(Long userId);
    byte[] generateMonthlyPostChart(Long userId);
    byte[] generateUserStarChart(Long userId);
    List<byte[]> generateEventStarCharts(Long userId);
}
