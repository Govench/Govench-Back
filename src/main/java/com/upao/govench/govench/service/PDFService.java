package com.upao.govench.govench.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.impl.GraphServiceImpl;
import com.upao.govench.govench.service.impl.UserStatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PDFService {
    @Autowired
    private GraphServiceImpl graphServiceImpl;

    public ByteArrayInputStream generateUserReportPdf(ReportResponseDTO reportResponseDTO, int userId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            System.out.println("Iniciando generación del PDF para el usuario con ID: " + userId);

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Título principal
            System.out.println("Agregando título principal al PDF...");
            Paragraph title = new Paragraph("Reporte de Usuario")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            System.out.println("Agregando tabla con resumen general...");
            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell resumenCell = new Cell()
                    .add(new Paragraph("Resumen General")
                            .setFontSize(14)
                            .setBold()
                            .setTextAlignment(TextAlignment.LEFT))
                    .add(new Paragraph("Seguidos: " + reportResponseDTO.getSummary().getNewFollowers()))
                    .add(new Paragraph("Conexiones Hechas: " + reportResponseDTO.getSummary().getConnectionsMade()))
                    .setBorder(Border.NO_BORDER);
            table.addCell(resumenCell);

            document.add(table);

            Paragraph invisibleParagraph = new Paragraph("\n");
            document.add(invisibleParagraph);

            // Generar gráfico de respuestas semanales
            System.out.println("Generando gráfico semanal...");
            byte[] weeklyChart = graphServiceImpl.generateWeeklyPostChart(userId);
            if (weeklyChart != null) {
                System.out.println("Gráfico semanal generado con éxito.");
                ImageData chartImageData = ImageDataFactory.create(weeklyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            } else {
                document.add(new Paragraph("\n"));
                System.out.println("No se pudo generar el gráfico semanal.");
            }

            // Generar gráfico de respuestas mensuales
            System.out.println("Generando gráfico mensual...");
            byte[] monthlyChart = graphServiceImpl.generateMonthlyPostChart(userId);
            if (monthlyChart != null) {
                System.out.println("Gráfico mensual generado con éxito.");
                ImageData chartImageData = ImageDataFactory.create(monthlyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            } else {
                document.add(new Paragraph("\n"));
                System.out.println("No se pudo generar el gráfico mensual.");
            }

            // Generar gráfico de valoración por estrellas
            System.out.println("Generando gráfico de valoración por estrellas...");
            byte[] starRatingChart = graphServiceImpl.generateUserStarChart(userId);
            if (starRatingChart != null) {
                System.out.println("Gráfico de valoración por estrellas generado con éxito.");
                ImageData chartImageData = ImageDataFactory.create(starRatingChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            } else {
                document.add(new Paragraph("\n"));
                System.out.println("No se pudo generar el gráfico de valoración por estrellas.");
            }

            // Generar gráficos de valoración de eventos
            System.out.println("Generando gráficos de valoración de eventos...");
            List<byte[]> eventRatingCharts = graphServiceImpl.generateEventStarCharts(userId);
            if (eventRatingCharts != null && !eventRatingCharts.isEmpty()) {
                System.out.println("Se generaron " + eventRatingCharts.size() + " gráficos de valoración de eventos.");
                for (byte[] chartBytes : eventRatingCharts) {
                    ImageData chartImageData = ImageDataFactory.create(chartBytes);
                    Image chartImage = new Image(chartImageData);
                    document.add(chartImage);
                    document.add(new Paragraph("\n"));
                }
            } else {
                document.add(new Paragraph("\n"));
                System.out.println("No se generaron gráficos de valoración de eventos.");
            }

            document.close();
            System.out.println("PDF generado con éxito.");
        } catch (Exception e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
