package com.hackathon.architectural_overview.service;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class DummyService {
    public String dumpData(String reqId){
        return "Rohit";
    }

}
