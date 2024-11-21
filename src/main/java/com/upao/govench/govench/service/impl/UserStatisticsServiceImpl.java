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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsServiceImpl implements UserStatisticsService {
    private EventRepository eventRepository;
    private RatingRepository ratingRepository;
    private PostRepository postRepository;
    private RatingEventRepository ratingEventRepository;

    @Override
    public Map<String, Integer> getWeeklyPost(Integer userId) {
        System.out.println("Obteniendo publicaciones semanales para el usuario con ID: " + userId);
        Map<String, Integer> weeklyResponses = new HashMap<>();

        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.println("Semana calculada: Inicio = " + startOfWeek + ", Fin = " + endOfWeek);

        for (LocalDate date = startOfWeek; !date.isEqual(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
            int dailyResponses = postRepository.countByAutor_IdAndCreated(userId, date);
            System.out.println("Publicaciones del día " + date + ": " + dailyResponses);
            weeklyResponses.put(date.getDayOfWeek().toString(), dailyResponses);
        }

        return weeklyResponses;
    }

    @Override
    public Map<String, Integer> getMonthlyPost(Integer userId) {
        System.out.println("Obteniendo publicaciones mensuales para el usuario con ID: " + userId);
        Map<String, Integer> monthlyResponses = new HashMap<>();

        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("Mes calculado: Inicio = " + startOfMonth + ", Fin = " + endOfMonth);

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            int dailyResponses = postRepository.countByAutor_IdAndCreated(userId, date);
            System.out.println("Publicaciones del día " + date + ": " + dailyResponses);
            monthlyResponses.put(date.getDayOfMonth() + "/" + date.getMonthValue(), dailyResponses);
        }

        return monthlyResponses;
    }

    @Override
    public Map<Integer, Integer> getUserRatings(Integer userId) {
        System.out.println("Obteniendo calificaciones del usuario con ID: " + userId);
        Map<Integer, Integer> starCounts = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            int count = ratingRepository.countByRatedUser_IdAndRatingValue(userId, i);
            System.out.println("Calificaciones de " + i + " estrellas: " + count);
            starCounts.put(i, count);
        }

        return starCounts;
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> getUserEventRatingsByEvent(Integer userId) {
        System.out.println("Obteniendo calificaciones por evento para el usuario con ID: " + userId);
        Map<Integer, Map<Integer, Integer>> eventRatingsMap = new HashMap<>();

        // Obtener todos los eventos creados por el usuario
        List<Event> events = eventRepository.findByOwner_Id(userId);
        System.out.println("Eventos creados por el usuario: " + events.size());

        // Iterar sobre cada evento
        for (Event event : events) {
            System.out.println("Procesando evento con ID: " + event.getId());
            Map<Integer, Integer> starCounts = new HashMap<>();

            // Contar las calificaciones para cada puntuación del 1 al 5
            for (int i = 1; i <= 5; i++) {
                int count = ratingEventRepository.countByEventId_IdAndValorPuntuacion(event.getId(), i);
                System.out.println("Calificaciones de " + i + " estrellas para el evento con ID " + event.getId() + ": " + count);
                starCounts.put(i, count);
            }

            // Agregar al mapa de eventos
            eventRatingsMap.put(event.getId(), starCounts);
        }

        return eventRatingsMap;
    }
}
