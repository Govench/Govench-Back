package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.PostRepository;
import com.upao.govench.govench.repository.RatingRepository;
import com.upao.govench.govench.service.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private PostRepository postRepository;

    public Map<String, Integer> getWeeklyPost(Long userId) {
        Map<String, Integer> weeklyResponses = new HashMap<>();

        // Obtener el rango para la semana actual
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));


        // Iterar sobre cada día de la semana
        for (LocalDate date = startOfWeek; !date.isEqual(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
            int dailyResponses = postRepository.countByAutor_IdAndCreated(userId, date);
            weeklyResponses.put(date.getDayOfWeek().toString(), dailyResponses);
        }

        return weeklyResponses;
    }

    public Map<String, Integer> getMonthlyPost(Long userId) {
        Map<String, Integer> monthlyResponses = new HashMap<>();

        // Obtener el rango para el mes actual
        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());


        // Iterar sobre cada día del mes
        for (LocalDate date = startOfMonth; !date.isEqual(endOfMonth.plusDays(1)); date = date.plusDays(1)) {
            int dailyResponses = postRepository.countByAutor_IdAndCreated(userId, date);
            monthlyResponses.put(date.getDayOfMonth() + "/" + date.getMonthValue(), dailyResponses);
        }

        return monthlyResponses;
    }

}
