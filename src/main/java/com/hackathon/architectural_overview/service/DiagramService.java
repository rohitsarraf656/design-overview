package com.hackathon.architectural_overview.service;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class DiagramService {

    public ResponseEntity<byte[]> generatePlantUmlDiagram(String plantUmlSyntax) throws IOException {
        SourceStringReader reader = new SourceStringReader(plantUmlSyntax);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/svg+xml")).body(os.toByteArray());
    }

    @Override
    public String toString() {
        return "DiagramService";
    }
}

