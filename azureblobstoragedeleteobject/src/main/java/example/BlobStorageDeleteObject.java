/**
 * BlobStorageDeleteObject is an example that handles Blob Storage containers on Microsoft Azure.
 * Delete a Blob in a Blob Storage container.
 * The connection string is taken from app.properties file.
 * You must use 2 parameters:
 * CONTAINER_NAME = Name of container
 * BLOB_NAME = Name of blob
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;


public class BlobStorageDeleteObject {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragedeleteobject.jar <CONTAINER_NAME> <BLOB_NAME>");
            System.exit(1);
        }

        // The name for the container
        String containerName = args[0];
        String blobName = args[1];
        System.out.printf("Blob Storage Container name: %s\n", containerName);
        System.out.printf("Blob name:                   %s\n", blobName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // Delete a Blob in a Blob Storage container
        deleteBlob(storageConnectionString, containerName, blobName);

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
     * Delete a Blob in a Blob Storage container.
     */
    private static void deleteBlob(String storageConnectionString, String containerName, String blobName) {
        try {
            System.out.println("Deleting Blob Storage container ...");

            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            // Get the container client object
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            if (containerClient.exists()) {
                // Get the blob client object
                BlobClient blobClient = containerClient.getBlobClient(blobName);
                if (blobClient.exists()) {
                    // Delete the blob
                    blobClient.delete();
                    System.out.println("Deleted");
                } else {
                    System.out.printf("Error: Blob \"%s\" does NOT exist.\n", blobName);
                }
            } else {
                System.out.printf("Error: Blob Storage container \"%s\" does NOT exist.\n", containerName);
            }

        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
