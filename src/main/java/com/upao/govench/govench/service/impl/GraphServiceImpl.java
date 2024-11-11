package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.service.GraphService;

import com.upao.govench.govench.service.UserStatisticsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class GraphServiceImpl implements GraphService {
    private UserStatisticsService userStatisticsService;
    private EventServiceImpl eventServiceImpl;

    public byte[] generateWeeklyPostChart(Integer userId) {
        // Obtenemos los datos de respuestas semanales
        Map<String, Integer> weeklyResponses = userStatisticsService.getWeeklyPost(userId);

        if (weeklyResponses.isEmpty() || weeklyResponses.values().stream().allMatch(count -> count == 0)) {
            return null;
        }

        // Creacion de dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Agregar los datos al dataset con traducción
        weeklyResponses.forEach((day, count) -> {
            dataset.addValue(count, "Publicaciones", day); // Usar el día tal como está
        });

        // Crear el gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Publicaciones por Día en la Semana",
                "Día",
                "Cantidad de Publicaciones",
                dataset
        );

        return createChartImage(chart);
    }

    public byte[] generateMonthlyPostChart(Integer userId) {
        // Obtenemos los datos de respuestas mensuales
        Map<String, Integer> monthlyResponses = userStatisticsService.getMonthlyPost(userId);

        // Verificar si hay datos y si al menos uno de los valores es mayor que 0
        if (monthlyResponses.isEmpty() || monthlyResponses.values().stream().allMatch(count -> count == 0)) {
            return null;
        }

        // Creamos el dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        monthlyResponses.forEach((period, count) -> dataset.addValue(count, "Publicaciones", period));

        // Crear el gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Publicaciones por Día en el Mes",
                "Día",
                "Cantidad de publicaciones",
                dataset
        );

        return createChartImage(chart);
    }

    public byte[] generateUserStarChart(Integer userId) {
        Map<Integer, Integer> userRatings = userStatisticsService.getUserRatings(userId);

        if (userRatings.values().stream().allMatch(count -> count == 0)) { // Verificar si todas las calificaciones son 0
            return null;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        userRatings.forEach((stars, count) -> dataset.addValue(
                count, "Calificaciones",
                stars + " Estrellas"));

        JFreeChart chart = ChartFactory.createBarChart(
                "Calificaciones del Usuario",
                "Estrellas",
                "Cantidad",
                dataset
        );

        return createChartImage(chart);
    }

    public List<byte[]> generateEventStarCharts(Integer userId) {
        // Obtener las calificaciones de eventos por usuario
        Map<Integer, Map<Integer, Integer>> eventStarCounts = userStatisticsService.getUserEventRatingsByEvent(userId);

        List<byte[]> chartImages = new ArrayList<>();

        // Crear los gráficos para cada evento
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : eventStarCounts.entrySet()) {
            Integer eventId = entry.getKey();
            Map<Integer, Integer> stars = entry.getValue();

            if (stars.values().stream().allMatch(count -> count == 0)) { // Verificar si todas las calificaciones son 0
                continue;
            }

            // Crear el dataset para el gráfico de este evento
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Map.Entry<Integer, Integer> starEntry : stars.entrySet()) {
                dataset.addValue(starEntry.getValue(), "Estrellas", starEntry.getKey() + " Estrellas");
            }

            // Obtener el título del evento
            String eventTitle = eventServiceImpl.getEventById(eventId).getTittle();

            // Crear el gráfico de barras
            JFreeChart chart = ChartFactory.createBarChart(
                    "Calificaciones de Estrellas para " + eventTitle,
                    "Estrellas",
                    "Cantidad de Calificaciones",
                    dataset
            );

            // Guardar la imagen del gráfico
            chartImages.add(createChartImage(chart));
        }

        return chartImages.isEmpty() ? null : chartImages; // Devuelve la lista de imágenes de gráficos
    }

    public byte[] createChartImage(JFreeChart chart) {
        try {
            ByteArrayOutputStream chartOutputStream = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(chartOutputStream, chart, 600, 400);
            return chartOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
