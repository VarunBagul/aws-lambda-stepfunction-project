package com.example.service;

import java.util.HashSet;
import java.util.Set;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

public class DuplicateCheckService {

//    // Simulating DynamoDB using in-memory set
//    private static final Set<String> processedEventIds = new HashSet<>();
//
//    public void checkDuplicate(String eventId) {
//
//        if (processedEventIds.contains(eventId)) {
//            throw new RuntimeException("Duplicate event: " + eventId);
//        }
//
//        // Mark as processed
//        processedEventIds.add(eventId);
//    }

    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "my-dev-challenge-test";
    private static final String INDEX_NAME = "eventId-index";

    public DuplicateCheckService() {
        this.dynamoDbClient = DynamoDbClient.create();
    }

    public void checkDuplicate(String eventId) {

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":eventId", AttributeValue.builder().s(eventId).build());

        QueryRequest request = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .indexName(INDEX_NAME)
                .keyConditionExpression("eventId = :eventId")
                .expressionAttributeValues(expressionValues)
                .build();

        QueryResponse response = dynamoDbClient.query(request);

        if (response.count() > 0) {
            throw new RuntimeException("Duplicate event: " + eventId);
        }
    }

}