package com.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.example.service.StepFunctionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.service.ValidationService;
import com.example.service.DuplicateCheckService;
import com.example.service.StatusService;
import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<DynamodbEvent, Void> {

    private final StepFunctionService stepService = new StepFunctionService();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ValidationService validationService = new ValidationService();

    private final DuplicateCheckService duplicateService = new DuplicateCheckService();
    private final StatusService statusService = new StatusService();

    @Override
    public Void handleRequest(DynamodbEvent event, Context context) {

        System.out.println("Lambda triggered!");

        event.getRecords().forEach(record -> {
            String messageId=null;
            String jsonData=null;
            String eventId=null;
            try {

                System.out.println("Event Name: " + record.getEventName());

                if (!"INSERT".equals(record.getEventName())) {
                    System.out.println("Skipping non-insert event");
                    return;
                }
                System.out.println("Full NewImage: " + record.getDynamodb().getNewImage());

                if (record.getDynamodb().getNewImage().get("jsonData") == null ||
                        record.getDynamodb().getNewImage().get("PK") == null) {

                    System.out.println("Missing required fields in DynamoDB record");
                    return;
                }
                jsonData = record.getDynamodb()
                        .getNewImage()
                        .get("jsonData")
                        .getS();

                messageId = record.getDynamodb()
                        .getNewImage()
                        .get("PK")
                        .getS();

                System.out.println("MessageId: " + messageId);
                System.out.println("Raw JSON: " + jsonData);

                JsonNode jsonNode = mapper.readTree(jsonData);
                //1a-Validate the Json
                validationService.validate(jsonNode);
                //1b-Extract values
                String eventType = jsonNode.get("header").get("eventType").asText();
                eventId = jsonNode.get("header").get("eventId").asText();

                //2- Duplicate check
                duplicateService.checkDuplicate(eventId);
                //String executionName = eventType + messageId;
                String executionName = eventType + messageId + "-" + System.currentTimeMillis();

                statusService.updateStatus(messageId,"IN_PROGRESS",eventId);
                if (executionName.length() > 80) {
                    executionName = executionName.substring(0, 80);
                }

                System.out.println("Execution Name: " + executionName);

                Map<String, Object> inputMap = new HashMap<>();
                inputMap.put("jsonData", jsonData);
                inputMap.put("messageId", messageId);

                stepService.startExecution(executionName, mapper.writeValueAsString(inputMap));
                //stepService.startExecution(executionName, jsonData);

            } catch (Exception e) {
                if(e.getMessage().contains("Duplicate")){
                    System.out.println("DUPLICATE ERROR: " + e.getMessage());
                    //System.out.println("Status = DUPLICATE");
                    statusService.updateStatus(messageId,"DUPLICATE",eventId);
                }else{
                    System.out.println("VALIDATION ERROR: " + e.getMessage());
                    if (messageId != null) {
                        statusService.updateStatus(messageId, "VALIDATION_ERROR",eventId);
                    }
                    System.out.println("Status = VALIDATION_ERROR");
                    //statusService.updateStatus(messageId,"VALIDATION_ERROR",eventId);
                }
            }
        });

        return null;
    }
}