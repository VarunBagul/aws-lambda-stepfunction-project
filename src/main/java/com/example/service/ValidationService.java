package com.example.service;

import com.fasterxml.jackson.databind.JsonNode;

public class ValidationService {

    public void validate(JsonNode jsonNode) {

        // Check header exists
        if (jsonNode.get("header") == null) {
            throw new RuntimeException("Missing 'header'");
        }

        // Check eventType exists
        if (jsonNode.get("header").get("eventType") == null) {
            throw new RuntimeException("Missing 'eventType'");
        }

        // Check eventId exists
        if (jsonNode.get("header").get("eventId") == null) {
            throw new RuntimeException("Missing 'eventId'");
        }
    }
}