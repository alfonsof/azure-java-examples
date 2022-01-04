/**
 * SenderEH is an example that handles Event Hubs on Microsoft Azure.
 * It handles an Event Hub and sends events to an event hub event stream.
 * The configuration for connecting to Event Hub is taken from app.properties file.
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;


public class SenderEH {
    private static String eventHubConnectionString = "";
    private static String eventHubName = "";

    public static void main(String[] args) {
        // Load Configuration from a file and get the Data Hub configuration
        loadConfiguration();

        // Publish events to Event Hub
        publishEvents();
    }

    /**
     * Load Configuration from a file and get the Storage Connection String.
     */
    private static void loadConfiguration() {

        // The connection string is taken from app.properties file
        Properties prop = new Properties();

        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("app.properties");
            prop.load(is);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        // Define the Event Hub connection-string with your values
        eventHubConnectionString = prop.getProperty("EventHubConnectionString");
        // Define the Event Hub name with your values
        eventHubName = prop.getProperty("EventHubName");
    }

    /**
     * Code for publishing events to Event Hub.
     * @throws IllegalArgumentException if the EventData is bigger than the max batch size.
     */
    public static void publishEvents() {
        System.out.println("Preparing batch of events ...");
        // create a producer client
        EventHubProducerClient producer = new EventHubClientBuilder()
                .connectionString(eventHubConnectionString, eventHubName)
                .buildProducerClient();

        // sample events in an array
        List<EventData> allEvents = Arrays.asList(new EventData("First event"),
                new EventData("Second event"), new EventData("Third event"));

        // create a batch
        EventDataBatch eventDataBatch = producer.createBatch();

        System.out.println("Sending batch of events to Event Hub...");
        for (EventData eventData : allEvents) {
            // try to add the event from the array to the batch
            if (!eventDataBatch.tryAdd(eventData)) {
                // if the batch is full, send it and then create a new batch
                producer.send(eventDataBatch);
                eventDataBatch = producer.createBatch();

                // Try to add that event that couldn't fit before.
                if (!eventDataBatch.tryAdd(eventData)) {
                    throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
                            + eventDataBatch.getMaxSizeInBytes());
                }
            }
        }
        // send the last batch of remaining events
        if (eventDataBatch.getCount() > 0) {
            producer.send(eventDataBatch);
        }
        System.out.println("Sent");
        producer.close();
    }
}
