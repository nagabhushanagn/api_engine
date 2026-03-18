/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.repository;

import com.amitron.api_engine.entity.GateDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Ngn
 */
@Repository
public class BaconRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BaconRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /* =====================================================
       CUSTOMER DETAIL
       ===================================================== */
    public Map<String, Object> getCustomerDetail(String jobName, String release) {

        /* =====================================================
       STEP 1: Get customer from HEADER
       ===================================================== */
        String headerSql
                = "SELECT \"H-CUST\", \"H-CUST-NBR\" "
                + "FROM HEADER "
                + "WHERE \"H-JOB#\" = ?";

        List<Map<String, Object>> headerList
                = jdbcTemplate.queryForList(headerSql, jobName);

        if (headerList.isEmpty()) {
            return null;
        }

        Map<String, Object> header = headerList.get(0);
        String custNbr = (String) header.get("H-CUST-NBR");

        /* =====================================================
       STEP 2: Get billing info from CUST
       ===================================================== */
        String custSql
                = "SELECT \"Bill-street-1\", \"Bill-city\", \"Bill-state\", "
                + "\"Bill-country\", \"Bill-zip\", \"Phone-area-code\", "
                + "\"Phone-number\", \"AccountingEmail\", \"TERMS\" "
                + "FROM CUST "
                + "WHERE \"Customer-nbr\" = ?";

        List<Map<String, Object>> custList
                = jdbcTemplate.queryForList(custSql, custNbr);

        Map<String, Object> result = new HashMap<>();

        // copy header fields
        result.putAll(header);

        // copy cust fields
        if (!custList.isEmpty()) {
            result.putAll(custList.get(0));
        }

        return result;
    }

    /* =====================================================
       JOB DETAIL
       ===================================================== */
    public Map<String, Object> getJobDetail(String jobName, String release) {

        Map<String, Object> result = new HashMap<>();

        /* =====================================================
       STEP 1 — HEADER
       ===================================================== */
        String headerSql
                = "SELECT \"H-PART#\", \"H-CURRENT-REV\", \"ITAR\", \"H-PC-ACCNT\" "
                + "FROM HEADER "
                + "WHERE \"H-JOB#\" = ?";

        List<Map<String, Object>> headerList
                = jdbcTemplate.queryForList(headerSql, jobName);

        if (!headerList.isEmpty()) {
            result.putAll(headerList.get(0));
        }

        /* =====================================================
       STEP 2 — RELEASE
       ===================================================== */
        String releaseSql
                = "SELECT \"R-NBR-LAYERS\", \"R-BOARDS-PER-PNL\", \"R-PNL-L\", \"R-PNL-W\", "
                + "\"R-PO-NBR\", \"R-PO-DATE\", \"R-STATUS\", \"R-DUE-QTY\", \"R-DUE-DATE\", \"MLZCY\", "
                + "\"R-COST\", \"R-LOT-PRICE\", \"R-TOOL-COST\", \"R-ADD-CHG-2\" "
                + "FROM release "
                + "WHERE \"R-JOB#\" = ? AND \"R-RELEASE-NBR\" = ?";

        List<Map<String, Object>> releaseList
                = jdbcTemplate.queryForList(releaseSql, jobName, release);

        if (!releaseList.isEmpty()) {
            result.putAll(releaseList.get(0));
        }

        /* =====================================================
       STEP 3 — TRAVHEAD
       ===================================================== */
        String travSql
                = "SELECT \"Matl-Lot-No\", \"Target-thickness\", \"Lam-thickness\", "
                + "\"Start_thickness\", \"End_Thickness\", \"Part-Size-L\", \"Part-Size-W\", "
                + "\"Array-Size-L\", \"Array-Size-W\", \"BoardsPerArray\", \"Arrays-per-panel\" "
                + "FROM travhead "
                + "WHERE \"JOB NBR\" = ?";

        List<Map<String, Object>> travList
                = jdbcTemplate.queryForList(travSql, jobName);

        if (!travList.isEmpty()) {
            result.putAll(travList.get(0));
        }

        return result;
    }

    /* =====================================================
       SHIPPING DETAIL
       ===================================================== */
    public Map<String, Object> getShippingDetail(String jobName, String release) {

        Map<String, Object> result = new HashMap<>();

        /* =====================================================
       STEP 1 — get packing slip number
       ===================================================== */
        String psDetailSql
                = "SELECT \"INVOICE/PACKING SLIP #\" "
                + "FROM psdetail "
                + "WHERE \"Job Nbr\" = ?";

        List<Map<String, Object>> psList
                = jdbcTemplate.queryForList(psDetailSql, jobName + release);

        if (psList.isEmpty()) {
            return result;
        }

        Object slipObj = psList.get(0).get("INVOICE/PACKING SLIP #");
        if (slipObj == null) {
            return result;
        }

        /* =====================================================
       STEP 2 — get shipping header
       ===================================================== */
        String psHeaderSql
                = "SELECT \"Packing_Slip_Date\", \"Ship_VIA\", \"Cust_Dock_Date\", "
                + "\"Shipper-Number\", \"Book_Date\" "
                + "FROM PSHEADER "
                + "WHERE \"Packing Slip Nbr\" = ?";

        List<Map<String, Object>> headerList
                = jdbcTemplate.queryForList(psHeaderSql, slipObj);

        // include slip number in result
        result.put("INVOICE/PACKING SLIP #", slipObj);

        if (!headerList.isEmpty()) {
            result.putAll(headerList.get(0));
        }

        return result;
    }

    /* =====================================================
       GATE DETAIL (MULTIPLE ROWS)
       ===================================================== */
    public List<Map<String, Object>> getGateDetail(String jobName, String release) {

        String sql
                = "SELECT "
                + "\"D-DATE\", "
                + "\"D-Time\", "
                + "\"D-DEST\", "
                + "\"D-QTY-MOVED\", "
                + "\"D-DATE-OUT\", "
                + "\"D-TIME-OUT\" "
                + "FROM arcship "
                + "WHERE \"D-JOB#\" = ?";

        String fullJob = jobName + release;

        List<Map<String, Object>> rows
                = jdbcTemplate.queryForList(sql, fullJob);

        for (Map<String, Object> row : rows) {
            Object gateObj = row.get("D-DEST");
            if (gateObj != null) {
                String gateId = gateObj.toString().trim();
                String gateName = getGateName(gateId);
                row.put("G-NAME", gateName);
            }
        }

        return rows;
    }

    private String getGateName(String gateId) {

        String sql
                = "SELECT \"G-NAME\" FROM gates WHERE \"G-GATE#\" LIKE ?";

        List<Map<String, Object>> list
                = jdbcTemplate.queryForList(sql, gateId);

        if (!list.isEmpty()) {
            Object name = list.get(0).get("G-NAME");
            return name != null ? name.toString() : null;
        }

        return null;
    }

    public List<String> getReleasesByJob(String jobName) {

        String sql
                = "SELECT \"R-RELEASE-NBR\" "
                + "FROM release "
                + "WHERE \"R-JOB#\" = ? "
                + "ORDER BY \"R-RELEASE-NBR\"";

        List<Map<String, Object>> rows
                = jdbcTemplate.queryForList(sql, jobName);

        List<String> releases = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Object rel = row.get("R-RELEASE-NBR");
            if (rel != null) {
                releases.add(rel.toString().trim());
            }
        }

        return releases;
    }

    public String getReleasesStatusCode(String jobName, String release) {
        String query = "SELECT \"R-RELEASE-STATUS\" FROM release WHERE \"R-JOB#\" = ? AND \"R-RELEASE-NBR\" = ?";

        List<String> result = jdbcTemplate.query(
                query,
                new Object[]{jobName, release},
                (rs, rowNum) -> rs.getString("R-RELEASE-STATUS")
        );

        return result.isEmpty() ? null : result.get(0);
    }

    public Map<String, List<String>> getReleasedJobList() {

        String query = "SELECT \"R-JOB#\", \"R-RELEASE-NBR\" " +
               "FROM release " +
               "WHERE (\"R-RELEASE-STATUS\" = 'Y' OR \"R-RELEASE-STATUS\" IS NULL) " +
               "AND \"R-RELEASE-NBR\" IS NOT NULL " +
               "AND \"R-DUE-DATE\" >= ? " +
               "AND \"R-JOB#\" NOT LIKE 'S%' " +
               "ORDER BY \"R-JOB#\"";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, java.sql.Date.valueOf("2026-01-01"));

        Map<String, List<String>> jobReleases = new LinkedHashMap<>();
System.out.println(rows.size() + " #$%^%$%$#");
        for (Map<String, Object> row : rows) {
            String job = (String) row.get("R-JOB#");
            Object releaseObj = row.get("R-RELEASE-NBR");
            if (releaseObj == null) {
                continue;  // skip invalid rows
            }
            String release = releaseObj.toString();

            jobReleases.computeIfAbsent(job, k -> new ArrayList<>()).add(release);
        }

        return jobReleases;
    }
}
