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
    @Override
    public byte[] generateChart() {
        try {
            // Crear el dataset (datos del gráfico)
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(5, "Eventos", "2020");
            dataset.addValue(7, "Eventos", "2021");
            dataset.addValue(9, "Eventos", "2022");

            // Crear el gráfico de barras
            JFreeChart chart = ChartFactory.createBarChart(
                    "Eventos por Año", // Título del gráfico
                    "Año",             // Eje X
                    "Total de Eventos", // Eje Y
                    dataset);

            // Guardar el gráfico en un ByteArrayOutputStream
            ByteArrayOutputStream chartOutputStream = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(chartOutputStream, chart, 600, 400);

            // Devolver el gráfico como byte[]
            return chartOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
