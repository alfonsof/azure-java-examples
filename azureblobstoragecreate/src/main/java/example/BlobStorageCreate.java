/**
 * BlobStorageCreate is an example that handles Blob Storage containers on Microsoft Azure.
 * Create a new Blob Storage container.
 * The connection string is taken from app.properties file.
 * You must use 1 parameter:
 * CONTAINER_NAME = Name of container
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;


public class BlobStorageCreate {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragecreate.jar <CONTAINER_NAME>");
            System.exit(1);
        }

        // The name for the new container
        String containerName = args[0];
        System.out.printf("Blob Storage Container name: %s\n", containerName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // Create a new Blob Storage container
        createContainer(storageConnectionString, containerName);
    }

    /**
    * Load Configuration from a file and get the Storage Connection String.
    */
    private static String loadConfiguration() {

        // The connection string is taken from app.properties file
        Properties prop = new Properties();

        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("app.properties");
            prop.load(is);
        } catch(IOException e) {
            System.out.println(e.toString());
        }
        String storageAccountConnectionString = prop.getProperty("StorageAccountConnectionString");

        return storageAccountConnectionString;
    }

    /**
     * Create a new Blob Storage container.
     */
    private static void createContainer(String storageConnectionString, String containerName) {
        try
        {
            System.out.println("Creating Blob Storage container ...");

            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            // Create the container and return a container client object
            BlobContainerClient containerClient = blobServiceClient.createBlobContainer(containerName);

            System.out.println("Created");
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
