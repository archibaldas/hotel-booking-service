package com.example.hotel_booking_service.statistics.export;

import com.example.hotel_booking_service.exception.CsvExportException;
import com.example.hotel_booking_service.statistics.event.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class StatisticExportService {

    private final StatisticRepository repository;

    public byte[] exportCsv(){

        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            CSVPrinter csvPrinter = new CSVPrinter(writer,
                    CSVFormat.DEFAULT
                            .withHeader(
                                    "type",
                                    "userId",
                                    "arrivalDate",
                                    "departureDate",
                                    "createdAt")
            )
        ) {
            repository.findAll().forEach(e ->
            {
                try {
                    csvPrinter.printRecord(e.getType(),
                            e.getUserId(),
                            e.getArrivalDate(),
                            e.getDepartureDate(),
                            e.getCreatedAt());
                } catch (IOException ex) {
                    throw new CsvExportException("Ошибка записи данных: " + ex);
                }
            });
            csvPrinter.flush();
            return out.toByteArray();
        } catch (IOException | CsvExportException exc) {
            throw new CsvExportException("Ошибка извлечения данных в CSV: " +  exc);
        }
    }
}
