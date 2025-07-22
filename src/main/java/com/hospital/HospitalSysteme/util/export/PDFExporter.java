package com.hospital.HospitalSysteme.util.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PDFExporter {

    public byte[] exportToPdf(String title, List<String> headers, List<List<String>> data,
                              Map<String, Object> metadata) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // Ajouter des métadonnées
            document.addTitle(title);
            document.addCreator("Système Hospitalier");
            document.addCreationDate();

            document.open();

            // Ajouter le titre
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(Chunk.NEWLINE);

            // Créer le tableau
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);

            // Ajouter les en-têtes
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Ajouter les données
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);
            for (List<String> row : data) {
                for (String cell : row) {
                    table.addCell(new Phrase(cell, dataFont));
                }
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            log.error("Erreur lors de l'exportation en PDF", e);
            throw new RuntimeException("Erreur lors de l'exportation en PDF", e);
        }
    }
}