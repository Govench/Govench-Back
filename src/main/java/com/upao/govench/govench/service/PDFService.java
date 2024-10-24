package com.upao.govench.govench.service;

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



            // Obtener estadísticas del usuario
            int totalEventsAttended = userStatisticsService.getTotalEventsAttendedByUser(userId);
            int totalFollowers = reportResponseDTO.getSummary().getNewFollowers();

            // Generar gráficos específicos
            byte[] eventChart = graphServiceImpl.generateEventAttendanceChart(totalEventsAttended);
            byte[] followersChart = graphServiceImpl.generateFollowersChart(totalFollowers);

            // Insertar gráficos en el PDF
            if (eventChart != null) {
                Image eventChartImage = new Image(ImageDataFactory.create(eventChart));
                document.add(eventChartImage);
            }

            if (followersChart != null) {
                Image followersChartImage = new Image(ImageDataFactory.create(followersChart));
                document.add(followersChartImage);
            }


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ByteArrayInputStream(out.toByteArray());
    }
}
