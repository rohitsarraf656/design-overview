package com.hackathon.architectural_overview.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class DiagramService {

//    public ResponseEntity<DiagramResponse> generatePlantUmlDiagram(String plantUmlSyntax) throws IOException {
////        SourceStringReader reader = new SourceStringReader(plantUmlSyntax);
////        final ByteArrayOutputStream os = new ByteArrayOutputStream();
////        reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
////        os.close();
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/svg+xml")).body(os.toByteArray());
//    }
public  String createPlantUmlSyntax(List<String> traceLines) {
    StringBuilder plantUml = new StringBuilder("@startuml\n");
    traceLines.forEach(t-> {
        plantUml.append(t);
        plantUml.append("\n");
    });
    plantUml.append("@enduml\n");
    return plantUml.toString();
}

}

