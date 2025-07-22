package com.hospital.HospitalSysteme.util.export;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class CSVExporter {

    public byte[] exportToCsv(List<String> headers, List<List<String>> data) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(headers.toArray(new String[0])));

            for (List<String> row : data) {
                csvPrinter.printRecord(row);
            }

            csvPrinter.flush();
            csvPrinter.close();

            return out.toByteArray();
        } catch (Exception e) {
            log.error("Erreur lors de l'exportation en CSV", e);
            throw new RuntimeException("Erreur lors de l'exportation en CSV", e);
        }
    }
}