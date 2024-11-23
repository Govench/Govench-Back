package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.service.GraphService;

import com.upao.govench.govench.service.UserStatisticsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    private UserStatisticsService userStatisticsService;
    @Autowired
    private EventRepository eventRepository;

    public byte[] generateWeeklyPostChart(Integer userId) {
        Map<String, Integer> weeklyResponses = userStatisticsService.getWeeklyPost(userId);

        if (weeklyResponses.isEmpty() || weeklyResponses.values().stream().allMatch(count -> count == 0)) {
            return null;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        weeklyResponses.forEach((day, count) -> dataset.addValue(count, "Publicaciones", day));

        JFreeChart chart = ChartFactory.createBarChart(
                "Publicaciones por día en durante la semana",
                "Día",
                "Cantidad de Publicaciones",
                dataset
        );
        chart.removeLegend();

        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        chart.getCategoryPlot().setRenderer(renderer);

        return createChartImage(chart);
    }

    public byte[] generateMonthlyPostChart(Integer userId) {
        Map<String, Integer> monthlyResponses = userStatisticsService.getMonthlyPost(userId);

        YearMonth currentMonth = YearMonth.now();
        int daysInMonth = currentMonth.lengthOfMonth();

        Map<String, Integer> completeMonthlyResponses = new LinkedHashMap<>();
        for (int day = 1; day <= daysInMonth; day++) {
            String dayLabel = day + "/" + currentMonth.getMonthValue(); // Formato del día (e.j., 1/11)
            completeMonthlyResponses.put(dayLabel, monthlyResponses.getOrDefault(dayLabel, 0));
        }

        if (monthlyResponses.isEmpty()) { return null; }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        completeMonthlyResponses.forEach((period, count) -> dataset.addValue(count, "Publicaciones", period));

        JFreeChart chart = ChartFactory.createBarChart(
                "Publicaciones en comunidades durante el " + currentMonth,
                "Día",
                "Cantidad de publicaciones",
                dataset
        );
        chart.removeLegend();

        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        chart.getCategoryPlot().setRenderer(renderer);

        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90); // Rotar las etiquetas 90 grados

        return createChartImage(chart);
    }

    public byte[] generateUserStarChart(Integer userId) {
        Map<Integer, Integer> userRatings = userStatisticsService.getUserRatings(userId);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        boolean hasData = false;

        for (int stars = 1; stars <= 5; stars++) {
            int count = userRatings != null ? userRatings.getOrDefault(stars, 0) : 0;
            dataset.addValue(count, "Calificaciones", stars + " Estrellas");
            if (count > 0) {
                hasData = true;
            }
        }

        if (!hasData) { return null; }

        JFreeChart chart = ChartFactory.createBarChart(
                "Calificaciones recibidas por otros usuarios",
                "Estrellas",
                "Cantidad",
                dataset
        );
        chart.removeLegend();

        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        chart.getCategoryPlot().setRenderer(renderer);

        return createChartImage(chart);
    }

    public List<byte[]> generateEventStarCharts(Integer userId) {
        Map<String, Map<Integer, Integer>> eventStarCounts = userStatisticsService.getUserEventRatingsByEvent(userId);
        List<byte[]> chartImages = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, Integer>> entry : eventStarCounts.entrySet()) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int stars = 1; stars <= 5; stars++) { // Inicializar con valores en 0 para todos los rangos de estrellas
                dataset.addValue(0, "Estrellas", stars + " Estrellas");
            }

            if (entry.getValue() != null) { // Sobrescribir valores si existen calificaciones
                entry.getValue().forEach((stars, count) -> dataset.addValue(count, "Estrellas", stars + " Estrellas"));
            }

            // Verificar si todos los valores en el dataset son 0
            if (entry.getValue() == null || entry.getValue().values().stream().allMatch(count -> count == 0)) {
                continue;
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Calificaciones para el evento " + entry.getKey(),
                    "Estrellas",
                    "Cantidad de Calificaciones",
                    dataset
            );
            chart.removeLegend();

            BarRenderer renderer = new BarRenderer();
            renderer.setSeriesPaint(0, Color.BLUE);
            chart.getCategoryPlot().setRenderer(renderer);

            chartImages.add(createChartImage(chart));
        }

        return chartImages.isEmpty() ? null : chartImages;
    }

    public byte[] generateEventParticipantsChart(Integer userId) {
        List<Object[]> eventParticipantsData = eventRepository.findEventsWithParticipantsCount(userId);

        if (eventParticipantsData.isEmpty()) {
            return null;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] data : eventParticipantsData) {
            String eventTitle = (String) data[0];
            long participantCount = (Long) data[1];
            dataset.addValue(participantCount, "Participantes", eventTitle);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Cantidad de Participantes por Evento",
                "Eventos",
                "Cantidad de Participantes",
                dataset
        );
        chart.removeLegend();

        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, Color.BLUE); // Color de la barra
        chart.getCategoryPlot().setRenderer(renderer);

        return createChartImage(chart);
    }


    public byte[] createChartImage(JFreeChart chart) {
        try (ByteArrayOutputStream chartOutputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(chartOutputStream, chart, 600, 400);
            return chartOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
