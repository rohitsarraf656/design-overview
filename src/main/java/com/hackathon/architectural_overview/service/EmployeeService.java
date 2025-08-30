package com.hackathon.architectural_overview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private DummyService dummyService;
    public String getName(String reqId){
        return dummyService.dumpData(reqId);
    }

    @Override
    public String toString() {
        return "EmployeeService";
    }

}
