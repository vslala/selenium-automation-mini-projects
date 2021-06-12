package com.sodastream.automationscripts.reportgenerators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CSVReportGenerator {

    private final String filename;

    public CSVReportGenerator(String filename) throws IOException {
        this.filename = filename;
        if (Path.of(filename).toFile().exists())
            Files.delete(Path.of(filename));

        Files.createFile(Path.of(filename));
    }

    public void addRecord(List<String> tuples) {
        var record = new StringBuilder(System.lineSeparator());
        tuples.forEach(tuple -> record.append(tuple).append(","));

        try(var bw = Files.newBufferedWriter(Path.of(this.filename), StandardOpenOption.APPEND)) {
            bw.write(record.substring(0, record.length() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
