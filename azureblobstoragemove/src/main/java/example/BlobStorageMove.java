/**
 * BlobStorageMove is an example that handles Blob Storage containers on Microsoft Azure.
 * Move a file from a Blob Storage container to another Blob Storage container.
 * The credentials are taken from AZURE_AUTH_LOCATION environment variable.
 * The connection string is taken from app.properties file.
 * You must use 3 parameters:
 * SOURCE_CONTAINER      = Source container name
 * SOURCE_FILE           = Source file name
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

public class BlobStorageMove {
    public static void main(String[] args) {
        String sourceContainerName;      // Source container name
        String sourceBlobName;           // Source blob
        String destinationContainerName; // Destination container name
        String destinationBlobName;      // Destination blob

        if (args.length < 3) {
            System.out.println("Not enough parameters. Proper Usage is: java -jar azureblobstoragemove.jar <SOURCE_CONTAINER> <SOURCE_FILE> <DESTINATION_CONTAINER>");
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

        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Get a reference to a source container.
            // The container name must be lower case
            CloudBlobContainer sourceContainer = blobClient.getContainerReference(sourceContainerName);

            // Get a reference to a destiny container.
            // The container name must be lower case
            CloudBlobContainer destinationContainer = blobClient.getContainerReference(destinationContainerName);

            // Get a reference to source Blob.
            CloudBlockBlob sourceBlob = sourceContainer.getBlockBlobReference(sourceBlobName);

            // Get a reference to destiny Blob.
            CloudBlockBlob destinationBlob = destinationContainer.getBlockBlobReference(destinationBlobName);

            // Copy the blob
            System.out.println(String.format("\nMoving blob: \"%s\".", sourceBlob.getUri().toURL()));
            System.out.println(String.format("to container: \"%s\".", destinationContainer.getUri().toURL()));
            destinationBlob.startCopy(sourceBlob);
            if (waitForCopyToComplete(destinationBlob) == CopyStatus.SUCCESS) {
                // Delete the blob
                sourceBlob.deleteIfExists();
                System.out.println("Successfully moved the blob.");
            } else {
                System.out.println("Not successfully moved the blob.");
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
}
