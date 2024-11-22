package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.repository.*;
import com.upao.govench.govench.service.UserStatisticsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsServiceImpl implements UserStatisticsService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RatingEventRepository ratingEventRepository;

    @Override
    public Map<String, Integer> getWeeklyPost(Integer userId) {
        Map<String, Integer> weeklyResponses = new HashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        for (LocalDate date = startOfWeek; !date.isEqual(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
            int dailyResponses = postRepository.countByAutor_IdAndCreated(userId, date);
            weeklyResponses.put(date.getDayOfWeek().toString(), dailyResponses);
        }
        return weeklyResponses.isEmpty() ? Collections.emptyMap() : weeklyResponses;
    }

    @Override
    public Map<String, Integer> getMonthlyPost(Integer userId) {
        Map<String, Integer> monthlyResponses = new HashMap<>();
        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            int dailyResponses = postRepository.countByAutor_IdAndCreated(userId, date);
            if (dailyResponses > 0) {
                monthlyResponses.put(date.getDayOfMonth() + "/" + date.getMonthValue(), dailyResponses);
            }
        }

        return monthlyResponses.isEmpty() ? Collections.emptyMap() : monthlyResponses;
    }

    @Override
    public Map<Integer, Integer> getUserRatings(Integer userId) {
        Map<Integer, Integer> starCounts = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            int count = ratingRepository.countByRatedUser_IdAndRatingValue(userId, i);
            if (count > 0) {
                starCounts.put(i, count);
            }
        }

        return starCounts.isEmpty() ? Collections.emptyMap() : starCounts;
    }

    @Override
    public Map<String, Map<Integer, Integer>> getUserEventRatingsByEvent(Integer userId) {
        Map<String, Map<Integer, Integer>> eventRatingsMap = new HashMap<>();
        List<Event> events = eventRepository.findByOwner_Id(userId);

        for (Event event : events) {
            Map<Integer, Integer> starCounts = new HashMap<>();

            for (int i = 1; i <= 5; i++) {
                int count = ratingEventRepository.countByEventId_IdAndValorPuntuacion(event.getId(), i);
                if (count > 0) {
                    starCounts.put(i, count);
                }
            }

            if (!starCounts.isEmpty()) { eventRatingsMap.put(event.getTittle(), starCounts); }
        }

        return eventRatingsMap.isEmpty() ? Collections.emptyMap() : eventRatingsMap;
    }
}
