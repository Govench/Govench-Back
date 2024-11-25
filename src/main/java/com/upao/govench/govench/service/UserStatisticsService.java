package com.upao.govench.govench.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface UserStatisticsService {
    Map<String, Integer>  getWeeklyPost(Integer userId);
    Map<String, Integer>  getMonthlyPost(Integer userId);
    Map<Integer, Integer> getUserRatings(Integer userId);
    Map<String, Map<Integer, Integer>> getUserEventRatingsByEvent(Integer userId);
}
