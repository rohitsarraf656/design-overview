package com.hackathon.architectural_overview.controller;

import com.hackathon.architectural_overview.aspect.SequenceDiagramAspect;
import com.hackathon.architectural_overview.dto.DiagramResponse;
import com.hackathon.architectural_overview.service.DiagramService;
import com.hackathon.architectural_overview.service.EmployeeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/diagram")
@RequiredArgsConstructor
@Data
public class DiagramController {

    private final SequenceDiagramAspect aspect;
    private final DiagramService diagramService;
    private final EmployeeService employeeService;
    // A temporary store for traces, mapped by a unique ID
    private final Map<String, List<String>> generatedTraces = new ConcurrentHashMap<>();

    @GetMapping("/start-trace")
    public ResponseEntity<String> startTrace() {
        String traceId = UUID.randomUUID().toString();
        aspect.clearTrace();
        try {
            generatedTraces.put(traceId, new ArrayList<>(aspect.getTrace()));
            return ResponseEntity.ok(traceId);
        } finally {
            aspect.clearTrace();
        }
    }

    @GetMapping(value = "/generate/{traceId}", produces = "application/json")
    public ResponseEntity<DiagramResponse> generateDiagram(@PathVariable String traceId) {
        DiagramResponse diagramResponse = new DiagramResponse();
        List<String> traceLines = generatedTraces.get(traceId);
        if (traceLines == null) {
            return ResponseEntity.notFound().build();
        }

        String plantUmlSyntax = diagramService.createPlantUmlSyntax(traceLines);
        diagramResponse.setPlantUmlString(plantUmlSyntax);
        return ResponseEntity.ok(diagramResponse);
    }
}

