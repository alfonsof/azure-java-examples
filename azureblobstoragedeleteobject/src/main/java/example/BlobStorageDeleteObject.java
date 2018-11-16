/**
 * BlobStorageDeleteObject is an example that handles Blob Storage containers on Microsoft Azure.
 * Delete a Blob in a Blob Storage container.
 * The credentials are taken from AZURE_AUTH_LOCATION environment variable.
 * The connection string is taken from app.properties file.
 * You must use 1 parameter:
 * CONTAINER_NAME = Name of container
 * BLOB_NAME = Name of blob
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

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

        try
        {
            System.out.println("Deleting Blob Storage container ...");

            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Get a reference to a container.
            // The container name must be lower case
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            if (container.exists()) {
                // Get a reference to a blob.
                CloudBlockBlob blob = container.getBlockBlobReference(blobName);
                if (blob.exists()) {
                    // Delete the blob if it exist.
                    blob.deleteIfExists();
                    System.out.println("Deleted");
                } else {
                    System.out.printf("Error: Blob \"%s\" does NOT exist.\n", blobName);
                }
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

    /**
     * Load Configuration from a file and get the Storage Connection String
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
        String defaultEndpointsProtocolStr = prop.getProperty("DefaultEndpointsProtocol");
        String accountNameStr = prop.getProperty("AccountName");
        String accountKeyStr = prop.getProperty("AccountKey");
        String endpointSuffixStr = prop.getProperty("EndpointSuffix");

        // Define the connection-string with your values
        String storageConnectionString =
                "DefaultEndpointsProtocol=" + defaultEndpointsProtocolStr + ";" +
                        "AccountName=" + accountNameStr + ";" +
                        "AccountKey="+ accountKeyStr + ";" +
                        "EndpointSuffix="+ endpointSuffixStr;

        return storageConnectionString;
    }
}
