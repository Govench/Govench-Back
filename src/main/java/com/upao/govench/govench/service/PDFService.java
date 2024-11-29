package com.upao.govench.govench.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.TextAlignment;
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
    @Autowired
    private ReportService reportService;

    public ByteArrayInputStream generateUserReportPdf(int userId) {

        ReportResponseDTO reportResponseDTO = reportService.generateReport(userId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        boolean hasValidData = false; // Bandera para validar si hay al menos un grafico

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

            byte[] weeklyChart = graphServiceImpl.generateWeeklyPostChart(userId);
            if (weeklyChart != null && weeklyChart.length > 0) {
                hasValidData = true;
                ImageData chartImageData = ImageDataFactory.create(weeklyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            byte[] monthlyChart = graphServiceImpl.generateMonthlyPostChart(userId);
            if (monthlyChart != null && monthlyChart.length > 0) {
                hasValidData = true;
                ImageData chartImageData = ImageDataFactory.create(monthlyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            byte[] starRatingChart = graphServiceImpl.generateUserStarChart(userId);
            if (starRatingChart != null) {
                hasValidData = true;
                ImageData chartImageData = ImageDataFactory.create(starRatingChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            List<byte[]> eventRatingCharts = graphServiceImpl.generateEventStarCharts(userId);
            if (eventRatingCharts != null && !eventRatingCharts.isEmpty()) {
                hasValidData = true;
                for (byte[] chartBytes : eventRatingCharts) {
                    ImageData chartImageData = ImageDataFactory.create(chartBytes);
                    Image chartImage = new Image(chartImageData);
                    document.add(chartImage);
                    document.add(new Paragraph("\n"));
                }
            }

            byte[] eventParticipantsChart = graphServiceImpl.generateEventParticipantsChart(userId);
            if (eventParticipantsChart != null) {
                hasValidData = true;
                ImageData chartImageData = ImageDataFactory.create(eventParticipantsChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            }

            document.close();

            if (!hasValidData) { return null; }
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
