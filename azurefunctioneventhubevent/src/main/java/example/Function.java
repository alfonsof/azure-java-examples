package com.alfonsof.azureexamples;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.*;

/**
 * Azure Functions with Event Hub trigger.
 */
public class Function {
    /**
     * This function will be invoked when an event is received from Event Hub.
     */
    @FunctionName("Function")
    public void run(
        @EventHubTrigger(name = "message", eventHubName = "myeventhub", connection = "MY_EVENT_HUB_IN", consumerGroup = "$Default", cardinality = Cardinality.MANY, dataType = "string") List<String> message,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Event Hub trigger function executed.");
        context.getLogger().info("Length:" + message.size());
        message.forEach(singleMessage -> context.getLogger().info(singleMessage));
    }
}
