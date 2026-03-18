/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
/**
 *
 * @author Ngn
 */
@Service
public class AdvantageTestService {

    private final JdbcTemplate jdbcTemplate;

    public AdvantageTestService(
        @Qualifier("advantageJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getJobs() {
        return jdbcTemplate.queryForList(
            "SELECT TOP 10 \"H-JOB#\" FROM HEADER"
        );
    }
}
