package com.hackathon.architectural_overview.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class EmployeeService {
    @Autowired
    private DummyService dummyService;
    public String getName(String reqId){
        return dummyService.dumpData(reqId);
    }

}
