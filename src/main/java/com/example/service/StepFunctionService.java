package com.example.service;

import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;
import software.amazon.awssdk.services.sfn.model.SfnException;

public class StepFunctionService {

    private final SfnClient sfnClient;

    // Constructor
    public StepFunctionService() {
        this.sfnClient = SfnClient.create(); // auto picks region + credentials
    }

    // actual ARN used
    private static final String STATE_MACHINE_ARN =
            "arn:aws:states:ap-south-1:336288635192:stateMachine:My-Dev-Challenge-Test";

    public void startExecution(String name, String input) {

        try {
            StartExecutionRequest request = StartExecutionRequest.builder()
                    .stateMachineArn(STATE_MACHINE_ARN)
                    .name(name)
                    .input(input)
                    .build();

            sfnClient.startExecution(request);

            System.out.println("Step Function triggered successfully!");

        } catch (SfnException e) {
            System.err.println("Error triggering Step Function: " + e.awsErrorDetails().errorMessage());
        }
    }
}