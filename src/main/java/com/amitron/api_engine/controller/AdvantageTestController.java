/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.controller;

import com.amitron.api_engine.service.AdvantageTestService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author Ngn
 */
@RestController
public class AdvantageTestController {
    private final AdvantageTestService service;

    public AdvantageTestController(AdvantageTestService service) {
        this.service = service;
    }

    @GetMapping("/advantage/test")
    public List<Map<String, Object>> test() {
        return service.getJobs();
    }
}
