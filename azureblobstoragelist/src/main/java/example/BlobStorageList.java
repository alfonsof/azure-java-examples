/**
 * BlobStorageList is an example that handles Blob Storage containers on Microsoft Azure.
 * List the blobs in a Blob Storage container.
 * The credentials are taken from AZURE_AUTH_LOCATION environment variable.
 * The connection string is taken from app.properties file.
 * You must use 1 parameter:
 * CONTAINER = Name of container
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class BlobStorageList {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough parameters. Proper Usage is: java -jar azureblobstoragelist.jar <CONTAINER_NAME>");
            System.exit(1);
        }

        // The name for the new container
        String containerName = args[0];

        // The connection string is taken from app.properties file
        Properties prop = new Properties();

        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("app.properties");
            prop.load(is);
        } catch (IOException e) {
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
                        "AccountKey=" + accountKeyStr + ";" +
                        "EndpointSuffix=" + endpointSuffixStr;
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Get a reference to a container.
            // The container name must be lower case
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            if (container.exists()) {
                System.out.printf("List of blobs in container %s:\n", containerName);
                // List the blobs in the container.
                for (ListBlobItem blobItem : container.listBlobs()) {
                    System.out.println("- Blob URI : " + blobItem.getUri());
                    if (blobItem instanceof CloudBlob) {
                        CloudBlob blob = (CloudBlob) blobItem;
                        System.out.println("       size: " +  blob.getProperties().getLength());
                        System.out.println("       type: " +  blob.getProperties().getBlobType());
                    }
                }
            } else {
                System.out.printf("Container %s does NOT exist.\n", containerName);
            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
