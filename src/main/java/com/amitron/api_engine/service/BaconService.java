/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.service;

import com.amitron.api_engine.entity.*;
import com.amitron.api_engine.repository.BaconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ngn
 */
@Service
public class BaconService {

    @Autowired
    private BaconRepository repo;

    public Map<String, Object> buildBaconJson(String jobName, String release) {

        Map<String, Object> response = new LinkedHashMap<>();

        /* ================= CUSTOMER ================= */
        Map<String, Object> custMap = repo.getCustomerDetail(jobName, release);
        CustomerDetail customer = mapCustomer(custMap);
        response.put("CustomerDetail", customer);

        /* ================= JOB ================= */
        Map<String, Object> jobMap = repo.getJobDetail(jobName, release);
        JobDetail job = mapJob(jobMap);
        response.put("JobDetail", job);

        /* ================= Price ================= same job detail repo is used*/
        PriceDetail price = mapPrice(jobMap);
        response.put("PriceDetail", price);

        /* ================= SHIPPING ================= */
        Map<String, Object> shipMap = repo.getShippingDetail(jobName, release);
        ShippingDetail shipping = mapShipping(shipMap);
        response.put("ShippingDetail", shipping);

        /* ================= GATE ================= */
        List<Map<String, Object>> gateMaps = repo.getGateDetail(jobName, release);
        List<GateDetail> gatesDetails = mapGateList(gateMaps);
        response.put("GateDetail", gatesDetails);

        return response;
    }

    /* =====================================================
       MAPPERS
       ===================================================== */
    private CustomerDetail mapCustomer(Map<String, Object> m) {
        if (m == null) {
            return null;
        }

        CustomerDetail c = new CustomerDetail();
        c.setCustomerName((String) m.get("H-CUST"));
        c.setCustomerNumber((String) m.get("H-CUST-NBR"));
        c.setBillAddress((String) m.get("Bill-street-1"));
        c.setCity((String) m.get("Bill-city"));
        c.setState((String) m.get("Bill-state"));
        c.setCountry((String) m.get("Bill-country"));
        c.setZipCode((String) m.get("Bill-zip"));
        c.setPhoneCode((String) m.get("Phone-area-code"));
        c.setPhoneNumber((String) m.get("Phone-number"));
        c.setEmail((String) m.get("AccountingEmail"));
        c.setTerm((String) m.get("TERMS"));
        return c;
    }

    private JobDetail mapJob(Map<String, Object> m) {
        if (m == null) {
            return null;
        }

        JobDetail j = new JobDetail();
        j.setPartNo((String) m.get("H-PART#"));
        j.setRevision((String) m.get("H-CURRENT-REV"));
        j.setLayerCount(m.get("R-NBR-LAYERS"));
        j.setPcbPerpanel(m.get("R-BOARDS-PER-PNL"));
        j.setPanelL(m.get("R-PNL-L"));
        j.setPanelW(m.get("R-PNL-W"));
        j.setPoNumber((String) m.get("R-PO-NBR"));
        j.setPoDate(m.get("R-PO-DATE"));
        j.setStatus((String) m.get("R-STATUS"));
        j.setDueQty(m.get("R-DUE-QTY"));
        j.setDueDate(m.get("R-DUE-DATE"));
        j.setMaterial((String) m.get("Matl-Lot-No"));
        j.setThickness(m.get("Target-thickness"));
        j.setSurfaceFinish(m.get("Lam-thickness"));
        j.setStartCopper(m.get("Start_thickness"));
        j.setEndCopper(m.get("End_Thickness"));
        j.setPcbSizeL(m.get("Part-Size-L"));
        j.setPcbSizeW(m.get("Part-Size-W"));
        j.setArraySizeL(m.get("Array-Size-L"));
        j.setArraySizeW(m.get("Array-Size-W"));
        j.setBoardsPerArray(m.get("BoardsPerArray"));
        j.setArraysPerPanel(m.get("Arrays-per-panel"));
        if (m.get("ITAR") != null && m.get("ITAR").equals("Y")) {
            j.setItar(true);
        } else {
            j.setItar(false);
        }
        j.setSalesPrioriry(m.get("MLZCY"));
        String plnt = toStr(m.get("H-PC-ACCNT"));

        if ("1".equals(plnt)) {
            j.setPlant("Amitron");
        } else if ("3".equals(plnt)) {
            j.setPlant("China");
        } else if (plnt != null) {
            j.setPlant("Other");
        } else {
            j.setPlant(null);
        }

        return j;
    }

    private PriceDetail mapPrice(Map<String, Object> m) {
        if (m == null) {
            return null;
        }
        PriceDetail p = new PriceDetail();
        p.setUnitPrice(m.get("R-COST"));
        p.setLotCharge(m.get("R-LOT-PRICE"));
        p.setToolingCharge(m.get("R-TOOL-COST"));
        p.setAddtioonalCharge(m.get("R-ADD-CHG-2"));

        return p;
    }

    private ShippingDetail mapShipping(Map<String, Object> m) {
        if (m == null || m.isEmpty()) {
            return null;
        }

        ShippingDetail s = new ShippingDetail();

        Object slip = m.get("INVOICE/PACKING SLIP #");
        s.setPackingSlipNumber(slip != null ? slip.toString() : null);

        s.setPackingSlipDate(m.get("Packing_Slip_Date"));
        s.setCarrier((String) m.get("Ship_VIA"));
        s.setCustDockDate(m.get("Cust_Dock_Date"));
        s.setTrackingID((String) m.get("Shipper-Number"));
        s.setBookDate(m.get("Book_Date"));

        return s;
    }

    private List<GateDetail> mapGateList(List<Map<String, Object>> list) {
        List<GateDetail> result = new ArrayList<>();
        if (list == null) {
            return result;
        }

        for (Map<String, Object> m : list) {
            GateDetail g = new GateDetail();

            g.setInDate(toStr(m.get("D-DATE")));
            g.setInTime(toStr(m.get("D-Time")));
            g.setGateID(toStr(m.get("D-DEST")));
            g.setGateName(toStr(m.get("G-NAME")));
            g.setQty(toStr(m.get("D-QTY-MOVED")));
            g.setOutDate(toStr(m.get("D-DATE-OUT")));
            g.setOutTime(toStr(m.get("D-TIME-OUT")));

            result.add(g);
        }
        return result;
    }

    private String toStr(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    public Map<String, Object> getReleases(String jobName) {

        Map<String, Object> response = new LinkedHashMap<>();

        List<String> releases = repo.getReleasesByJob(jobName);

        response.put("jobName", jobName);
        response.put("releases", releases);

        return response;
    }

    public Map<String, Object> getMultiReleasesJobData(String jobName) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Step 1: get releases
        Map<String, Object> releases = getReleases(jobName);
        List<String> releaseList = (List<String>) releases.get("releases");

        response.put("jobName", jobName);
        response.put("releaseCount", releaseList.size());

        // List to hold all release data
        List<Map<String, Object>> jobDataList = new ArrayList<>();

        for (String release : releaseList) {

            Map<String, Object> eachReleaseData = new LinkedHashMap<>();

            // Step 2: get job data
            Map<String, Object> jobData = buildBaconJson(jobName, release);

            eachReleaseData.put("release", release);
            eachReleaseData.put("data", jobData);

            jobDataList.add(eachReleaseData);
        }

        response.put("jobData", jobDataList);

        return response;
    }

    public Map<String, Object> getReleaseStatusCode(String jobName, String release) {
        Map<String, Object> response = new LinkedHashMap<>();

        String satusCode = repo.getReleasesStatusCode(jobName, release);

        response.put("statusCode", satusCode);

        return response;
    }

    public Map<String, Object> getReleasedJobList() {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, List<String>> releases = repo.getReleasedJobList();

        //response.put("jobName", jobName);
        response.put("released", releases);

        return response;
    }
/*
    public Map<String, Object> getReleasedJobsData() {

        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> jobsList = new ArrayList<>();

        // Step 1: Get released jobs with releases
        Map<String, List<String>> jobReleases = repo.getReleasedJobList();

        // Step 2: Loop through each job
        for (Map.Entry<String, List<String>> entry : jobReleases.entrySet()) {

            String jobName = entry.getKey();
            List<String> releases = entry.getValue();

            Map<String, Object> jobObj = new LinkedHashMap<>();
            jobObj.put("jobName", jobName);
            jobObj.put("releaseCount", releases.size());

            List<Map<String, Object>> jobDataList = new ArrayList<>();

            // Step 3: Loop releases
            for (String release : releases) {

                Map<String, Object> relObj = new LinkedHashMap<>();

                // Call existing method (VERY GOOD reuse)
                Map<String, Object> releaseData = buildBaconJson(jobName, release);

                relObj.put("release", release);
                relObj.put("data", releaseData);

                jobDataList.add(relObj);
            }

            jobObj.put("jobData", jobDataList);
            jobsList.add(jobObj);
        }

        response.put("jobs", jobsList);

        return response;
    }
*/
    public Map<String, Object> getReleasedJobsData(String jobNumber) {

        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> jobsList = new ArrayList<>();

        Map<String, List<String>> jobReleases = repo.getReleasedJobList();

        for (Map.Entry<String, List<String>> entry : jobReleases.entrySet()) {

            String jobName = entry.getKey();

            // FILTER LOGIC
            if (jobNumber != null && !jobNumber.equals(jobName)) {
                continue;
            }

            List<String> releases = entry.getValue();

            Map<String, Object> jobObj = new LinkedHashMap<>();
            jobObj.put("jobName", jobName);
            jobObj.put("releaseCount", releases.size());

            List<Map<String, Object>> jobDataList = new ArrayList<>();

            for (String release : releases) {
                try {
                    Map<String, Object> releaseData = buildBaconJson(jobName, release);

                    Map<String, Object> relObj = new LinkedHashMap<>();
                    relObj.put("release", release);
                    relObj.put("data", releaseData);

                    jobDataList.add(relObj);

                } catch (Exception e) {
                    System.out.println("Error for job: " + jobName + " release: " + release);
                    e.printStackTrace();
                }
            }

            jobObj.put("jobData", jobDataList);
            jobsList.add(jobObj);
        }

        response.put("jobs", jobsList);

        return response;
    }
}
