package com.upao.govench.govench.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.impl.GraphServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PDFService {
    @Autowired
    private GraphServiceImpl graphServiceImpl;

    public ByteArrayInputStream generateUserReportPdf(ReportResponseDTO reportResponseDTO, int userId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            Paragraph title = new Paragraph("Reporte de Usuario")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell resumenCell = new Cell() .add(new Paragraph("Resumen General")
                            .setFontSize(14)
                            .setBold()
                            .setTextAlignment(TextAlignment.LEFT));

            boolean hasData = false;

            if (reportResponseDTO.getSummary().getNewFollowers() > 0) {
                resumenCell.add(new Paragraph("Seguidos: " + reportResponseDTO.getSummary().getNewFollowers()));
                hasData = true;
            }

            if (reportResponseDTO.getSummary().getConnectionsMade() > 0) {
                resumenCell.add(new Paragraph("Conexiones Hechas: " + reportResponseDTO.getSummary().getConnectionsMade()));
                hasData = true;
            }

            double averageRating = reportResponseDTO.getSummary().getAverageRating();
            if (averageRating > 0) {
                resumenCell.add(new Paragraph("Calificación Promedio: " + String.format("%.2f", averageRating)));
                hasData = true;
            }

            if (hasData) {
                table.addCell(resumenCell);
                document.add(table);
            } else {
                document.add(new Paragraph("No hay datos disponibles para el resumen general.")
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(10));
            }

            byte[] weeklyChart = graphServiceImpl.generateWeeklyPostChart(userId);
            if (weeklyChart != null && weeklyChart.length > 0) {
                ImageData chartImageData = ImageDataFactory.create(weeklyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            byte[] monthlyChart = graphServiceImpl.generateMonthlyPostChart(userId);
            if (monthlyChart != null && monthlyChart.length > 0) {
                ImageData chartImageData = ImageDataFactory.create(monthlyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            byte[] starRatingChart = graphServiceImpl.generateUserStarChart(userId);
            if (starRatingChart != null) {
                ImageData chartImageData = ImageDataFactory.create(starRatingChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            List<byte[]> eventRatingCharts = graphServiceImpl.generateEventStarCharts(userId);
            if (eventRatingCharts != null && !eventRatingCharts.isEmpty()) {
                for (byte[] chartBytes : eventRatingCharts) {
                    ImageData chartImageData = ImageDataFactory.create(chartBytes);
                    Image chartImage = new Image(chartImageData);
                    document.add(chartImage);
                    document.add(new Paragraph("\n"));
                }
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
