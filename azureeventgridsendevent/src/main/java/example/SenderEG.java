/**
 * SenderEG is an example that handles Event Grids on Microsoft Azure.
 * It handles an Event Grid and sends events to an Event Grid Topic.
 * The configuration for connecting to Event Grid is taken from app.properties file.
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.azure.messaging.eventgrid.EventGridEvent;


public class SenderEG {
    private static String eventGridTopicKey = "";
    private static String eventGridTopicEndpoint = "";

    public static void main(String[] args) {
        // Load Configuration from a file and get the Data Grid configuration
        loadConfiguration();

        // Publish events to Event Grid
        publishEvents();
    }

    /**
     * Load Configuration from a file and get the information for connecting to Event Grid.
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
        // Define the Event Grid Topic key with your value
        eventGridTopicKey = prop.getProperty("EventGridTopicKey");
        // Define the Event Grid Topic endpoint with your value
        eventGridTopicEndpoint = prop.getProperty("EventGridTopicEndpoint");
    }

    /**
     * Code for publishing events to Event Grid.
     */
    public static void publishEvents() {
        System.out.println("Preparing batch of events ...");
        // For EventGridEvent
        EventGridPublisherClient<EventGridEvent> eventGridEventClient = new EventGridPublisherClientBuilder()
                .endpoint(eventGridTopicEndpoint)
                .credential(new AzureKeyCredential(eventGridTopicKey))
                .buildEventGridEventPublisherClient();

        // Make sure that the event grid topic or domain you're sending to accepts EventGridEvent schema.
        List<EventGridEvent> events = new ArrayList<>();

        HashMap<String, String> cars = new HashMap<String, String>();
        cars.put("make", "Audi");
        cars.put("model", "Q5");
        EventGridEvent eventGridEvent = new EventGridEvent(
                "myapp/vehicles/cars",
                "recordInserted",
                BinaryData.fromObject(cars),
                "1.0");

        events.add(eventGridEvent);
        System.out.println("Sending batch of events to Event Grid ...");
        eventGridEventClient.sendEvents(events);
        System.out.println("Sent");
    }
}
