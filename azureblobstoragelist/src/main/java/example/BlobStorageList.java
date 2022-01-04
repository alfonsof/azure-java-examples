/**
 * BlobStorageList is an example that handles Blob Storage containers on Microsoft Azure.
 * List the blobs in a Blob Storage container.
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
import com.azure.storage.blob.models.BlobItem;


public class BlobStorageList {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragelist.jar <CONTAINER_NAME>");
            System.exit(1);
        }

        // The name for the new container
        String containerName = args[0];
        System.out.printf("Blob Storage container name: %s\n", containerName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // List the Blobs in a Blob Storage container
        listContainerBlobs(storageConnectionString, containerName);
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
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        String storageAccountConnectionString = prop.getProperty("StorageAccountConnectionString");

        return storageAccountConnectionString;
    }

    /**
     * List the Blobs in a Blob Storage container.
     */
    private static void listContainerBlobs(String storageConnectionString, String containerName) {
        try {
            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            // Get the container client object
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            if (containerClient.exists()) {
                // List the blobs in the container
                for (BlobItem blobItem : containerClient.listBlobs()) {
                    System.out.println("- Blob URI : " + blobItem.getName());
                    System.out.println("       size: " + blobItem.getProperties().getContentLength());
                    System.out.println("       type: " + blobItem.getProperties().getBlobType());
                }
            } else {
                System.out.printf("Error: Container \"%s\" does NOT exist.\n", containerName);
            }
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
