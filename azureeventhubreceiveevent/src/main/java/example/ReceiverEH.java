/**
 * ReceiverEH is an example that handles Event Hubs on Microsoft Azure.
 * It handles an Event Hub and receive events from an event hub event stream.
 * The configuration for connecting to Event Hub is taken from app.properties file.
 */

package example;


import java.util.function.Consumer;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.messaging.eventhubs.models.PartitionContext;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.storage.blob.*;


public class ReceiverEH {
    private static String eventHubConnectionString = "<Event Hubs namespace connection string>";
    private static String eventHubName = "<Event hub name>";
    private static String storageAccountConnectionString = "<Storage connection string>";
    private static String storageAccountContainerName = "<Storage container name>";

    public static void main(String[] args) throws Exception {
        // Load Configuration from a file and get the Data Grid configuration
        loadConfiguration();

        // Receive events from Event Hub
        receiveEvents();
    }

    /**
     * Load Configuration from a file and get the Storage Connection String
     */
    private static void loadConfiguration() {

        // The connection string is taken from app.properties file
        Properties prop = new Properties();

        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("app.properties");
            prop.load(is);
        } catch(IOException e) {
            System.out.println(e.toString());
        }
        // Define the Event Hub connection-string with your values
        eventHubConnectionString = prop.getProperty("EventHubConnectionString");
        // Define the Event Hub name with your values
        eventHubName = prop.getProperty("EventHubName");
        // Define the Storage account connection string with your values
        storageAccountConnectionString = prop.getProperty("StorageAccountConnectionString");
        // Define the Storage account container name with your values
        storageAccountContainerName = prop.getProperty("StorageAccountContainerName");
    }

    /**
     * Code for receiving events from Event Hub.
     */
    public static void receiveEvents() throws Exception {

        // Create a blob container client that you use later to build an event processor client to receive and process events
        BlobContainerAsyncClient blobContainerAsyncClient = new BlobContainerClientBuilder()
                .connectionString(storageAccountConnectionString)
                .containerName(storageAccountContainerName)
                .buildAsyncClient();

        // Create a builder object that you will use later to build an event processor client to receive and process events and errors.
        EventProcessorClientBuilder eventProcessorClientBuilder = new EventProcessorClientBuilder()
                .connectionString(eventHubConnectionString, eventHubName)
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .processEvent(PARTITION_PROCESSOR)
                .processError(ERROR_HANDLER)
                .checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient));

        // Use the builder object to create an event processor client
        EventProcessorClient eventProcessorClient = eventProcessorClientBuilder.buildEventProcessorClient();

        System.out.println("Starting event processor ...");
        System.out.println("Waiting for an event ...");
        eventProcessorClient.start();

        System.out.println("Press enter to stop.");
        System.in.read();

        System.out.println("Stopping event processor ...");
        eventProcessorClient.stop();
        System.out.println("Event processor stopped.");

        System.out.println("Exiting process");
    }

    /**
     * Helper method that process events to the Receiver class.
     */
    public static final Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
        PartitionContext partitionContext = eventContext.getPartitionContext();
        EventData eventData = eventContext.getEventData();

        System.out.printf("Processing event from partition %s with sequence number %d with body: %s%n",
                partitionContext.getPartitionId(), eventData.getSequenceNumber(), eventData.getBodyAsString());

        // Every 10 events received, it will update the checkpoint stored in Azure Blob Storage.
        if (eventData.getSequenceNumber() % 10 == 0) {
            eventContext.updateCheckpoint();
        }
    };

    /**
     * Helper method that process errors to the Receiver class.
     */
    public static final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
        System.out.printf("Error occurred in partition processor for partition %s:\n %s.%n",
                errorContext.getPartitionContext().getPartitionId(),
                errorContext.getThrowable());
    };
}
