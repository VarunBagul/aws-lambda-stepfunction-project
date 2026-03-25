package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class ResponseLambda implements RequestHandler<Map<String, Object>, Void> {

    @Override
    public Void handleRequest(Map<String, Object> input, Context context) {

        System.out.println("Lambda 3 - Response Builder Started");

        try {
            String eventId = (String) input.get("eventId");
            String status = (String) input.get("status");

            System.out.println("Final EventId: " + eventId);
            System.out.println("Final Status: " + status);

            // Here you will push to Kinesis (later)
            System.out.println("Pushing to Kinesis... (mock)");

            // Update DynamoDB (reuse your StatusService)
            System.out.println("Updating final status in DynamoDB");

        } catch (Exception e) {
            System.out.println("Error in Lambda 3: " + e.getMessage());
        }

        return null;
    }
}