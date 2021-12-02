/**
 * BlobStorageDelete is an example that handles Blob Storage containers on Microsoft Azure.
 * Delete a Blob Storage container.
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

public class BlobStorageDelete {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragedelete.jar <CONTAINER_NAME>");
            System.exit(1);
        }

        // The name for the container
        String containerName = args[0];
        System.out.printf("Blob Storage Container name: %s\n", containerName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // Delete a Blob Storage container
        deleteContainer(storageConnectionString, containerName);
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
     * Delete a Blob Storage container.
     */
    private static void deleteContainer(String storageConnectionString, String containerName) {
        try
        {
            System.out.println("Deleting Blob Storage container ...");

            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            // Get the container client object
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            if (containerClient.exists()) {
                // Delete the container
                containerClient.delete();
                System.out.println("Deleted");
            } else {
                System.out.printf("Error: Blob Storage container \"%s\" does NOT exist.\n", containerName);
            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
