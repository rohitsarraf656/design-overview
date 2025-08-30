package com.hackathon.architectural_overview.service;

import org.springframework.stereotype.Service;

@Service
public class DummyService {
    public String dumpData(String reqId){
        return "Rohit";
    }

    @Override
    public String toString() {
        return "DummyService";
    }
}
