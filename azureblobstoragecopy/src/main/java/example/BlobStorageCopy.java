/**
 * BlobStorageCopy is an example that handles Blob Storage containers on Microsoft Azure.
 * Copy a Blob from a Blob Storage container to another Blob Storage container.
 * The credentials are taken from AZURE_AUTH_LOCATION environment variable.
 * The connection string is taken from app.properties file.
 * You must use 3 parameters:
 * SOURCE_CONTAINER      = Source container name
 * SOURCE_BLOB           = Source blob name
 * DESTINATION_CONTAINER = Destination container name
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.CopyStatus;

public class BlobStorageCopy {
    public static void main(String[] args) {
        String sourceContainerName;      // Source container name
        String sourceBlobName;           // Source blob
        String destinationContainerName; // Destination container name
        String destinationBlobName;      // Destination blob

        if (args.length < 3) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragecopy.jar <SOURCE_CONTAINER> <SOURCE_BLOB> <DESTINATION_CONTAINER>");
            System.exit(1);
        }

        sourceContainerName      = args[0];
        sourceBlobName           = args[1];
        destinationContainerName = args[2];
        destinationBlobName      = sourceBlobName;
        System.out.println("From - container: " + sourceContainerName);
        System.out.println("From - blob:      " + sourceBlobName);
        System.out.println("To   - container: " + destinationContainerName);
        System.out.println("To   - blob:      " + destinationBlobName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Get a reference to a source container.
            // The container name must be lower case
            CloudBlobContainer sourceContainer = blobClient.getContainerReference(sourceContainerName);

            if (sourceContainer.exists()) {
                // Get a reference to source Blob.
                CloudBlockBlob sourceBlob = sourceContainer.getBlockBlobReference(sourceBlobName);

                if (sourceBlob.exists()) {
                    // Get a reference to a destiny container.
                    // The container name must be lower case
                    CloudBlobContainer destinationContainer = blobClient.getContainerReference(destinationContainerName);

                    if (destinationContainer.exists()) {
                        // Get a reference to destiny Blob.
                        CloudBlockBlob destinationBlob = destinationContainer.getBlockBlobReference(destinationBlobName);
                        // Copy the blob
                        System.out.println(String.format("\nCopying Blob: \"%s\".", sourceBlob.getUri().toURL()));
                        System.out.println(String.format("to container: \"%s\".", destinationContainer.getUri().toURL()));
                        destinationBlob.startCopy(sourceBlob);

                        if (waitForCopyToComplete(destinationBlob) == CopyStatus.SUCCESS) {
                            System.out.printf("Blob \"%s\" copied\n", sourceBlobName);
                        } else {
                            System.out.printf("Error: Blob \"%s\" NOT copied.\n", sourceBlobName);
                        }

                    } else {
                        System.out.printf("Error: Destination Blob Storage container \"%s\" does NOT exist.\n", destinationContainerName);
                    }
                } else {
                    System.out.printf("Error: Source Blob \"%s\" does NOT exist.\n", sourceBlobName);
                }
            } else {
                System.out.printf("Error: Source Blob Storage container \"%s\" does NOT exist.\n", sourceContainerName);
            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }

    /**
     * Wait until the copy is completed.
     *
     * @param blob Target of the copy operation
     *
     * @throws InterruptedException
     * @throws StorageException
     */
    private static CopyStatus waitForCopyToComplete(CloudBlob blob) throws InterruptedException, StorageException {
        CopyStatus copyStatus = CopyStatus.PENDING;
        while (copyStatus == CopyStatus.PENDING) {
            Thread.sleep(1000);
            blob.downloadAttributes();
            copyStatus = blob.getCopyState().getStatus();
        }

        return copyStatus;
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
