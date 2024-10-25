package com.upao.govench.govench.service;

import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface GraphService {
    byte[] generateWeeklyPostChart(Long userId);
    byte[] generateMonthlyPostChart(Long userId);
}
