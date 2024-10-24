package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.service.GraphService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class GraphServiceImpl implements GraphService {

    public byte[] generateEventAttendanceChart(int totalEventsAttended) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Aquí asumimos que tenemos un total por año, en este caso solo uno
        dataset.addValue(totalEventsAttended, "Eventos Asistidos", "2024");

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Eventos Asistidos por Usuario",
                "Usuario",
                "Total de Eventos",
                dataset
        );

        return createChartImage(chart);
    }

    public byte[] generateCommunitiesCreatedChart(int totalCommunitiesCreated) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(totalCommunitiesCreated, "Comunidades Creadas", "2024");

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Comunidades Creadas por Usuario",
                "Usuario",
                "Total de Comunidades",
                dataset
        );

        return createChartImage(chart);
    }
    public byte[] generateFollowersChart(int totalFollowers) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(totalFollowers, "Seguidores", "2024");

        // Crear el gráfico de seguidores
        JFreeChart chart = ChartFactory.createBarChart(
                "Seguidores por Usuario",
                "Usuario",
                "Total de Seguidores",
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
