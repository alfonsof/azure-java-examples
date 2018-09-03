/**
 * BlobStorageUpload is an example that handles Blob Storage containers on Microsoft Azure.
 * Upload a local file to a Blob Storage container in an Azure storage account.
 * The credentials are taken from AZURE_AUTH_LOCATION environment variable.
 * The connection string is taken from app.properties file.
 * You must use 3 parameters:
 * CONTAINER_NAME   = Name of container
 * BLOB_NAME        = Blob name in the container
 * LOCAL_FILE_NAME  = Local file name
 */

package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class BlobStorageUpload {
    public static void main(String[] args) {
        String containerName;     // Container name
        String blobName;          // Blob name
        String localFileName;     // Local file name

        if (args.length < 3) {
            System.out.println("Not enough parameters.\nProper Usage is: java -jar azureblobstorageupload.jar <CONTAINER_NAME> <BLOB_NAME> <LOCAL_FILE_NAME>");
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
                File f = new File(localFileName);
                if (f.exists()) {
                    System.out.println("Uploading a local file to a Blob Storage container ...");
                    // Get a reference to Blob.
                    CloudBlockBlob blob = container.getBlockBlobReference(blobName);
                    File sourceFile = new File(localFileName);
                    // Upload local file
                    blob.upload(new FileInputStream(sourceFile), sourceFile.length());
                    System.out.println("Uploaded");
                } else {
                    System.out.printf("Error: Local file \"%s\" does NOT exist.", localFileName);
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
