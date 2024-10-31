package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserStatisticsService {
    Map<String, Integer>  getWeeklyPost(Integer userId);
    Map<String, Integer>  getMonthlyPost(Integer userId);
    Map<Integer, Integer> getUserRatings(Integer userId);
    Map<Integer, Map<Integer, Integer>> getUserEventRatingsByEvent(Integer userId);
}
