/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.controller;

import com.amitron.api_engine.service.BaconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ngn
 */
@RestController
@RequestMapping("/api/baconjobdata")
public class JobController {

    @Autowired
    private BaconService baconService;

    /**
     * Example:
     * http://localhost:8080/api/baconjobdata/job?jobName=103010&release=-001
     */
    @GetMapping("/job")
    public Map<String, Object> getJobFullData(
            @RequestParam String jobName,
            @RequestParam String release) {

        return baconService.buildBaconJson(jobName, release);
    }

    /**
     * Example: http://localhost:8080/api/baconjobdata/releases?jobName=103010
     */
    @GetMapping("/releases")
    public Map<String, Object> getReleases(
            @RequestParam String jobName) {

        return baconService.getReleases(jobName);
    }

    /**
     * Example:
     * http://localhost:8080/api/baconjobdata/job-with-releases?jobName=103010
     */
    @GetMapping("/job-with-releases")
    public Map<String, Object> getMultiReleasesJobData(
            @RequestParam String jobName) {

        return baconService.getMultiReleasesJobData(jobName);
    }

    /**
     * http://localhost:8080/api/baconjobdata/release-status?jobName=103010&release=-001
     *
     * @param jobName
     * @param release
     * @return
     */
    @GetMapping("/release-status")
    public Map<String, Object> getReleaseStatus(
            @RequestParam String jobName,
            @RequestParam String release) {

        return baconService.getReleaseStatusCode(jobName, release);
    }

    /**
     * http://localhost:8080/api/baconjobdata/released-joblist
     *
     * @return
     */
    @GetMapping("/released-joblist")
    public Map<String, Object> getReleasedJobList() {
        return baconService.getReleasedJobList();
    }

    /**
     * http://localhost:8080/api/baconjobdata/released-jobsdata
     *
     * @return
     *//*
    @GetMapping("/released-jobsdata")
    public Map<String, Object> getReleasedJobsData() {
        return baconService.getReleasedJobsData();
    }
*/
    /**
     * http://localhost:8080/api/baconjobdata/released-jobsdata?jobName=103010
     * @param jobNumber
     * @return 
     */
    @GetMapping("/released-jobsdata")
    public Map<String, Object> getReleasedJobsData(
            @RequestParam(required = false) String jobName) {

        return baconService.getReleasedJobsData(jobName);
    }
}
