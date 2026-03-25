package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ValidationCalculatorLambda implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {

        System.out.println("Lambda 2 - Validation & Calculation Started");

        Map<String, Object> response = new HashMap<>();

        try {
            String jsonData = (String) input.get("jsonData");

            JsonNode node = mapper.readTree(jsonData);

            String eventId = node.get("header").get("eventId").asText();

            // 🔹 MOCK API RESPONSE (instead of real API)
            int creditLimit = 1000;
            int thresholdPercent = 80;

            // 🔹 MOCK TRANSACTION DATA
            int amountSpent = 900;

            double usedPercent = (amountSpent * 100.0) / creditLimit;

            String status;

            if (amountSpent > creditLimit) {
                status = "CREDIT_LIMIT_EXCEEDED";
            } else if (amountSpent == creditLimit) {
                status = "CREDIT_LIMIT_REACHED";
            } else if (usedPercent > thresholdPercent) {
                status = "THRESHOLD_EXCEEDED";
            } else {
                status = "OK";
            }

            System.out.println("Calculated Status: " + status);

            response.put("eventId", eventId);
            response.put("status", status);
            response.put("amountSpent", amountSpent);
            response.put("creditLimit", creditLimit);

        } catch (Exception e) {
            System.out.println("Error in Lambda 2: " + e.getMessage());
            response.put("status", "FAILED");
        }

        return response;
    }
}