package com.hackathon.architectural_overview.controller;

import com.hackathon.architectural_overview.aspect.SequenceDiagramAspect;
import com.hackathon.architectural_overview.service.DiagramService;
import com.hackathon.architectural_overview.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/diagram")
public class DiagramController {
    @Autowired
    private SequenceDiagramAspect aspect;
    @Autowired
    private DiagramService diagramService;

    @Autowired
    private EmployeeService employeeService;
    // A temporary store for traces, mapped by a unique ID
    private final Map<String, List<String>> generatedTraces = new ConcurrentHashMap<>();

    @GetMapping("/start-trace")
    public ResponseEntity<String> startTrace() {
        String traceId = UUID.randomUUID().toString();
        aspect.clearTrace();
        String reqId = UUID.randomUUID().toString();
        String name = employeeService.getName(reqId);
        try {
            generatedTraces.put(traceId, new ArrayList<>(aspect.getTrace()));

            return ResponseEntity.ok(traceId);
        } finally {
            aspect.clearTrace();
        }
    }

    @GetMapping(value = "/generate/{traceId}", produces = "image/svg+xml")
    public ResponseEntity<byte[]> generateDiagram(@PathVariable String traceId) throws IOException {
        List<String> traceLines = generatedTraces.get(traceId);
        if (traceLines == null) {
            return ResponseEntity.notFound().build();
        }

        String plantUmlSyntax = createPlantUmlSyntax(traceLines);
        return diagramService.generatePlantUmlDiagram(plantUmlSyntax);
    }

    private String createPlantUmlSyntax(List<String> traceLines) {
        StringBuilder plantUml = new StringBuilder("@startuml\n");
        traceLines.forEach(t-> {
            plantUml.append(t);
            plantUml.append("\n");
        });
        plantUml.append("@enduml\n");
        return plantUml.toString();
    }

    @Override
    public String toString() {
        return "DiagramController";
    }
}

