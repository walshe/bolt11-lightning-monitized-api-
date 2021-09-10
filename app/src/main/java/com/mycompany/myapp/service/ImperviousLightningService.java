package com.mycompany.myapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ImperviousLightningService {

    private final static String IMPERVIOUS_NODE_URL = "http://127.0.0.1:8882";

    private RestTemplate restTemplate = new RestTemplate();

    public boolean isBoltInvoicePaid(String boltInvoice) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            JSONObject body = new JSONObject();
            body.put("invoice", boltInvoice);

            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
            String responseEntityStr = restTemplate.postForObject(IMPERVIOUS_NODE_URL + "/v1/lightning/checkinvoice", request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseEntityStr);

            return root.get("paid").asBoolean();

        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }


    }

    public String generateInvoice(Long sats) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            JSONObject body = new JSONObject();
            body.put("amount", sats);

            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
            String responseEntityStr = restTemplate.postForObject(IMPERVIOUS_NODE_URL + "/v1/lightning/generateinvoice", request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseEntityStr);

            return root.get("invoice").asText();

        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}
