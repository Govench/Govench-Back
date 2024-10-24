package com.upao.govench.govench.service;

import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface GraphService {
    byte[] generateEventAttendanceChart(int totalEventsAttended);
    byte[] generateCommunitiesCreatedChart(int totalCommunitiesCreated);
    byte[] generateFollowersChart(int totalFollowers);
}
