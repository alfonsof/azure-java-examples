/**
 * BlobStorageMove is an example that handles Blob Storage containers on Microsoft Azure.
 * Move a Blob from a Blob Storage container to another Blob Storage container.
 * The connection string is taken from app.properties file.
 * You must use 3 parameters:
 * SOURCE_CONTAINER      = Source container name
 * SOURCE_BLOB           = Source Blob name
 * DESTINATION_CONTAINER = Destination container name
 */

package example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import java.time.OffsetDateTime;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;


public class BlobStorageMove {
    public static void main(String[] args) {
        String sourceContainerName;      // Source container name
        String sourceBlobName;           // Source blob name
        String destinationContainerName; // Destination container name
        String destinationBlobName;      // Destination blob name

        if (args.length < 3) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragemove.jar <SOURCE_CONTAINER> <SOURCE_BLOB> <DESTINATION_CONTAINER>");
            System.exit(1);
        }

        sourceContainerName = args[0];
        sourceBlobName = args[1];
        destinationContainerName = args[2];
        destinationBlobName = sourceBlobName;
        System.out.println("From - container: " + sourceContainerName);
        System.out.println("From - blob:      " + sourceBlobName);
        System.out.println("To   - container: " + destinationContainerName);
        System.out.println("To   - blob:      " + destinationBlobName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // Move a Blob from a Blob Storage container to another Blob Storage container.
        moveBlob(storageConnectionString, sourceContainerName, sourceBlobName, destinationContainerName);
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
     * Move a Blob from a Blob Storage container to another Blob Storage container.
     */
    private static void moveBlob(String storageConnectionString,
                                 String sourceContainerName,
                                 String sourceBlobName,
                                 String destinationContainerName) {
        try {
            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            // Get the source container client object
            BlobContainerClient sourceContainerClient = blobServiceClient.getBlobContainerClient(sourceContainerName);

            if (sourceContainerClient.exists()) {
                // Get a reference to the source blob
                BlobClient sourceBlobClient = sourceContainerClient.getBlobClient(sourceBlobName);
                if (sourceBlobClient.exists()) {
                    // Get the destination container client object
                    BlobContainerClient destinationContainerClient = blobServiceClient.getBlobContainerClient(destinationContainerName);
                    if (destinationContainerClient.exists()) {
                        // Get a reference to the destination blob
                        BlobClient destinationBlobClient = destinationContainerClient.getBlobClient(sourceBlobName);

                        System.out.println(String.format("\nMoving Blob: \"%s\"", sourceBlobClient.getBlobUrl()));
                        System.out.println(String.format("from container: \"%s\"", sourceContainerClient.getBlobContainerUrl()));
                        System.out.println(String.format("to container: \"%s\"", destinationContainerClient.getBlobContainerUrl()));

                        // Create the SAS Token to get the permission to access the source blob
                        String sasToken = generateSasToken(sourceBlobClient);

                        // Copy the blob
                        destinationBlobClient.copyFromUrl(sourceBlobClient.getBlobUrl() + "?" + sasToken);

                        // Delete the blob
                        sourceBlobClient.delete();

                        System.out.println("Moved");
                    } else {
                        System.out.printf("Error: Destination Blob Storage container \"%s\" does NOT exist.\n", destinationContainerName);
                    }
                } else {
                    System.out.printf("Error: Source Blob \"%s\" does NOT exist.\n", sourceBlobName);
                }
            } else {
                System.out.printf("Error: Source Blob Storage container \"%s\" does NOT exist.\n", sourceContainerName);
            }
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }

    /**
     * Create the SAS Token to get the permission to access a blob.
     */
    private static String generateSasToken(BlobClient blobClient) {
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);

        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permission)
                .setStartTime(OffsetDateTime.now());
        String sasToken = blobClient.generateSas(values);

        return sasToken;
    }
}