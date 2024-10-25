package com.upao.govench.govench.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.impl.GraphServiceImpl;
import com.upao.govench.govench.service.impl.UserStatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class PDFService {
    @Autowired
    private UserStatisticsServiceImpl userStatisticsService;
    @Autowired
    private GraphServiceImpl graphServiceImpl;

    public ByteArrayInputStream generateUserReportPdf(ReportResponseDTO reportResponseDTO, int userId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Reporte de Usuario"));
            document.add(new Paragraph("Total de Eventos: " + reportResponseDTO.getSummary().getTotalEvents()));
            document.add(new Paragraph("Nuevos Seguidores: " + reportResponseDTO.getSummary().getNewFollowers()));
            document.add(new Paragraph("Conexiones Hechas: " + reportResponseDTO.getSummary().getConnectionsMade()));
            document.add(new Paragraph("Eventos Asistidos: " + reportResponseDTO.getSummary().getEventsAttended()));

            document.add(new Paragraph("Estadísticas de la Comunidad:"));
            document.add(new Paragraph("Total de Comunidades: " + reportResponseDTO.getSummary().getCommunityStats().getTotalCommunities()));
            document.add(new Paragraph("Total de Usuarios en Comunidades: " + reportResponseDTO.getSummary().getCommunityStats().getTotalUsersInCommunities()));
            document.add(new Paragraph("Total de Publicaciones en Comunidades: " + reportResponseDTO.getSummary().getCommunityStats().getTotalPostsInCommunities()));

            // Generar gráfico de respuestas semanales
            byte[] weeklyChart = graphServiceImpl.generateWeeklyPostChart((long) userId);
            if (weeklyChart != null) {
                ImageData chartImageData = ImageDataFactory.create(weeklyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            } else {
                System.out.println("No hay datos suficientes para generar algún gráfico");
            }

            // Generar gráfico de respuestas mensuales
            byte[] monthlyChart = graphServiceImpl.generateMonthlyPostChart((long) userId);
            if (monthlyChart != null) {
                ImageData chartImageData = ImageDataFactory.create(monthlyChart);
                Image chartImage = new Image(chartImageData);
                document.add(chartImage);
            } else {
                System.out.println("No hay datos suficientes para generar algún gráfico");
            }




            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ByteArrayInputStream(out.toByteArray());
    }
}
