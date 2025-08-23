package me.reply.be_gtt;

import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public List<String[]> read(String filename) {

        List<String[]> records = new ArrayList<>();

        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStream is = resource.getInputStream();

            // Read a CSV file and collect the lines into a list
            CSVReader reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                records.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
