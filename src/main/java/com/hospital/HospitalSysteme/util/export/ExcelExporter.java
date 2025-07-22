package com.hospital.HospitalSysteme.util.export;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ExcelExporter {

    public byte[] exportToExcel(String sheetName, List<String> headers, List<List<String>> data,
                                Map<String, Object> metadata) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            // Créer le style pour les en-têtes
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Créer la ligne d'en-tête
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Remplir les données
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> rowData = data.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }

            // Ajuster la largeur des colonnes
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Écrire dans le flux de sortie
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            return out.toByteArray();
        } catch (Exception e) {
            log.error("Erreur lors de l'exportation en Excel", e);
            throw new RuntimeException("Erreur lors de l'exportation en Excel", e);
        }
    }
}