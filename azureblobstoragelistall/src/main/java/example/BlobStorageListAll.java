/**
 * BlobStorageListAll is an example that handles Blob Storage containers on Microsoft Azure.
 * List the blobs in all Blob Storage containers.
 * The connection string is taken from app.properties file.
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;


public class BlobStorageListAll {
    public static void main(String[] args) {
        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // List all Blob Storage containers
        listContainers(storageConnectionString);
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
     * List all Blob Storage containers.
     */
    private static void listContainers(String storageConnectionString) {

        try
        {
            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            System.out.printf("List of Blob Storage containers:\n");

            // Enumerate all containers and list all blobs
            blobServiceClient.listBlobContainers().forEach(containerItem -> {
                String containerName = containerItem.getName();
                System.out.printf("- List of blobs in Blob Storage container \"%s\":\n", containerName);

                // Get the container client object
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

                // List the blobs in the container
                for (BlobItem blobItem : containerClient.listBlobs()) {
                    System.out.println("  - Blob URI : " + blobItem.getName());
                }
            });
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }

        /*
                try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            System.out.printf("List of Blob Storage containers:\n");

            // Enumerate all containers and list all blobs
            for (CloudBlobContainer container : blobClient.listContainers()) {
                System.out.printf("- List of blobs in Blob Storage container \"%s\":\n", container.getName());
                // List the blobs in the container.
                for (ListBlobItem blobItem : container.listBlobs()) {
                    System.out.println("  - Blob URI : " + blobItem.getUri());
                }
            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }

         */
    }
}
