package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.service.GraphService;

import com.upao.govench.govench.service.UserStatisticsService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    private UserStatisticsService userStatisticsService;

    public byte[] generateMonthlyPostChart(Long userId) {
        // Obtenemos los datos de respuestas mensuales
        Map<String, Integer> monthlyResponses = userStatisticsService.getMonthlyPost(userId);

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

    public byte[] generateWeeklyPostChart(Long userId) {
        // Obtenemos los datos de respuestas semanales
        Map<String, Integer> weeklyResponses = userStatisticsService.getWeeklyPost(userId);

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
