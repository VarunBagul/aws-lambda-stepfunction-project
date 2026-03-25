package com.example;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.example.handler.LambdaHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting local test...");

        // STEP 1: Create event
        DynamodbEvent event = new DynamodbEvent();

        // STEP 2: Initialize records list (VERY IMPORTANT)
        event.setRecords(new ArrayList<>());

        // STEP 3: Create record
        DynamodbEvent.DynamodbStreamRecord record = new DynamodbEvent.DynamodbStreamRecord();

        // STEP 4: Create stream record
        StreamRecord streamRecord = new StreamRecord();

        // STEP 5: Create newImage map
        /*
        Map<String, AttributeValue> newImage = new HashMap<>();

        newImage.put("PK", new AttributeValue().withS("msg-123"));

        newImage.put("jsonData",
                new AttributeValue().withS("{\"header\":{\"eventType\":\"CREATE\",\"eventId\":\"123\"}}"));
        */
        // First record
        DynamodbEvent.DynamodbStreamRecord record1 = new DynamodbEvent.DynamodbStreamRecord();
        StreamRecord streamRecord1 = new StreamRecord();

        Map<String, AttributeValue> newImage1 = new HashMap<>();
        newImage1.put("PK", new AttributeValue().withS("msg-123"));
        newImage1.put("jsonData",
                new AttributeValue().withS("{\"header\":{\"eventType\":\"CREATE\",\"eventId\":\"123\"}}"));

        streamRecord1.setNewImage(newImage1);
        record1.setDynamodb(streamRecord1);

        // Second record (same eventId → duplicate)
        DynamodbEvent.DynamodbStreamRecord record2 = new DynamodbEvent.DynamodbStreamRecord();
        StreamRecord streamRecord2 = new StreamRecord();

        Map<String, AttributeValue> newImage2 = new HashMap<>();
        newImage2.put("PK", new AttributeValue().withS("msg-456"));
        newImage2.put("jsonData",
                new AttributeValue().withS("{\"header\":{\"eventType\":\"CREATE\",\"eventId\":\"123\"}}"));

        streamRecord2.setNewImage(newImage2);
        record2.setDynamodb(streamRecord2);

        // Add both
        event.getRecords().add(record1);
        event.getRecords().add(record2);

        // STEP 6: Attach data
        //streamRecord.setNewImage(newImage);
        //record.setDynamodb(streamRecord);
        streamRecord2.setNewImage(newImage2);
        record2.setDynamodb(streamRecord2);

        // STEP 7: Add record to event
        //event.getRecords().add(record);
        event.getRecords().add(record1);
        event.getRecords().add(record2);

        // STEP 8: Call Lambda
        new LambdaHandler().handleRequest(event, null);
    }
}