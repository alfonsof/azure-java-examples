/**
 * BlobStorageDownload is an example that handles Blob Storage containers on Microsoft Azure.
 * Download a Blob from a Blob Storage container in an Azure storage account.
 * The connection string is taken from app.properties file.
 * You must use 3 parameters:
 * CONTAINER_NAME   = Name of container
 * BLOB_NAME        = Blob name in the container
 * LOCAL_FILE_NAME  = Local file name
 */

package example;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;


public class BlobStorageDownload {
    public static void main(String[] args) {
        String containerName;     // Container name
        String blobName;          // Blob name
        String localFileName;     // Local file name

        if (args.length < 3) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstoragedownload.jar <CONTAINER_NAME> <BLOB_NAME> <LOCAL_FILE_NAME>");
            System.exit(1);
        }

        containerName = args[0];
        blobName = args[1];
        localFileName = args[2];
        System.out.printf("Blob Storage Container name:  %s\n", containerName);
        System.out.printf("Blob name:                    %s\n", blobName);
        System.out.printf("Local file name:              %s\n", localFileName);

        // Load Configuration from a file and get the Storage Connection String
        String storageConnectionString = loadConfiguration();

        // Download a blob to a blob storage container.
        downloadBlob(storageConnectionString, containerName, blobName, localFileName);
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
     * Download a blob from a blob storage container.
     */
    private static void downloadBlob(String storageConnectionString, String containerName,
                                     String blobName, String localFileName) {

        try {
            // Create a BlobServiceClient object which will be used to create a container client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();

            // Get a reference to the container and return a container client object
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            if (containerClient.exists()) {
                // Get a reference to a blob
                BlobClient blobClient = containerClient.getBlobClient(blobName);
                if (blobClient.exists()) {
                    System.out.println("Downloading a Blob from a Blob Storage container to a local file ...");
                    File destinationFile = new File(localFileName);
                    // Download Blob
                    blobClient.downloadToFile(destinationFile.getAbsolutePath());
                    System.out.println("Downloaded");
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