package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserStatisticsService {
    Map<String, Integer>  getWeeklyPost(Long userId);
    Map<String, Integer>  getMonthlyPost(Long userId);
}
