package com.sodastream.automationscripts.reportgenerators;

import com.google.common.io.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVReportGeneratorTest {

    private CSVReportGenerator csv;

    @BeforeEach
    void setup() throws IOException {
        csv = new CSVReportGenerator("test.csv");
    }

    @Test
    void itShouldCreateFileAsSoonAsTheObjectIsInstantiated() {
        assertTrue(Path.of("test.csv").toFile().exists());
    }

    @Test
    void itShouldAppendRowRecordToTheFile() throws IOException {
        csv.addRecord(List.of("this", "is", "stored", "in", "different", "column", "."));

        var lines = Files.readLines(new File("test.csv"), StandardCharsets.UTF_8).get(0);

        assertEquals(lines, "this,is,stored,in,different,column,.");
    }

}