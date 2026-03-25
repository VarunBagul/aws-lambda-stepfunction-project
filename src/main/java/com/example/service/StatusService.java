package com.example.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
public class StatusService {

    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "my-dev-challenge-test";

    public StatusService() {
        this.dynamoDbClient = DynamoDbClient.create();
    }
    public void updateStatus(String messageId, String status, String eventId) {

//        long startTime = System.currentTimeMillis();
//
//        long ttl = Instant.now().plusSeconds(7 * 24 * 60 * 60).getEpochSecond();
//
//        System.out.println("=== Updating Status ===");
//        System.out.println("MessageId: " + messageId);
//        System.out.println("Status: " + status);
//        System.out.println("StartTime: " + startTime);
//        System.out.println("TTL (7 days): " + ttl);
        long startTime = System.currentTimeMillis();
        long ttl = Instant.now().plusSeconds(7 * 24 * 60 * 60).getEpochSecond();

        //Map<String, AttributeValue> item = new HashMap<>();

//        item.put("PK", AttributeValue.builder().s(messageId).build());
//        item.put("status", AttributeValue.builder().s(status).build());
//        item.put("startTime", AttributeValue.builder().n(String.valueOf(startTime)).build());
//        item.put("ttl", AttributeValue.builder().n(String.valueOf(ttl)).build());
//        item.put("eventId", AttributeValue.builder().s(eventId).build());
        Map<String, AttributeValue> item = new HashMap<>();

        item.put("PK", AttributeValue.builder().s(messageId).build());
        item.put("status", AttributeValue.builder().s(status).build());

        if (eventId != null) {
            item.put("eventId", AttributeValue.builder().s(eventId).build());
        }
        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);

        System.out.println("Status updated in DynamoDB: " + status);
    }
}