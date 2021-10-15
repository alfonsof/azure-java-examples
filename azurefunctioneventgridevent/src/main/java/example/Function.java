package com.alfonsof.azureexamples;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Event Grid trigger.
 */
public class Function {
    /**
     * This function will be invoked when an event is received from Event Grid.
     */
    @FunctionName("Function")
    public void run(@EventGridTrigger(name = "eventGridEvent") String message, final ExecutionContext context) {
        context.getLogger().info("Java Event Grid trigger function executed.");
        context.getLogger().info(message);
    }
}
