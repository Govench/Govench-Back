package com.upao.govench.govench.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.service.PDFService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@AllArgsConstructor
public class PDFServiceImpl implements PDFService {
    @Autowired
    private GraphServiceImpl graphServiceImpl;

    @Override
    public ByteArrayInputStream generateUserReportPdf(ReportResponseDTO reportResponseDTO) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Create PdfWriter instance
            PdfWriter writer = new PdfWriter(out);

            // Create PdfDocument instance
            PdfDocument pdfDocument = new PdfDocument(writer);

            // Pass PdfDocument to the Document constructor
            Document document = new Document(pdfDocument);

            // Add content to PDF
            document.add(new Paragraph("Reporte de Usuario"));
            document.add(new Paragraph("Total de Eventos: " + reportResponseDTO.getSummary().getTotalEvents()));
            document.add(new Paragraph("Nuevos Seguidores: " + reportResponseDTO.getSummary().getNewFollowers()));
            document.add(new Paragraph("Conexiones Hechas: " + reportResponseDTO.getSummary().getConnectionsMade()));
            document.add(new Paragraph("Eventos Asistidos: " + reportResponseDTO.getSummary().getEventsAttended()));

            // Add community stats
            document.add(new Paragraph("Estadísticas de la Comunidad:"));
            document.add(new Paragraph("Total de Comunidades: " + reportResponseDTO.getSummary().getCommunityStats().getTotalCommunities()));
            document.add(new Paragraph("Total de Usuarios en Comunidades: " + reportResponseDTO.getSummary().getCommunityStats().getTotalUsersInCommunities()));
            document.add(new Paragraph("Total de Publicaciones en Comunidades: " + reportResponseDTO.getSummary().getCommunityStats().getTotalPostsInCommunities()));

            // Obtener el gráfico generado por el GraphService
            byte[] chartBytes = graphServiceImpl.generateChart();
            if (chartBytes != null) {
                // Insertar el gráfico en el PDF
                Image chartImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(chartBytes));
                document.add(chartImage);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
