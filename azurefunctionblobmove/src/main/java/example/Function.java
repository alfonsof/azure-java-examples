package example;

import java.nio.charset.StandardCharsets;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobOutput;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;


/**
 * Azure Functions with Azure Blob trigger and move a Blob.
 */
public class Function {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path.
     * The blob contents are provided as input to this function.
     * Move the blob when it appears in a blob storage to another blob storage.
     */

    static final String sourceContainerName = "samples-workitems";

    @FunctionName("BlobMove")
    public void run(
            @BlobTrigger(
                connection = "MY_STORAGE_IN",
                name = "inputblob",
                path = "samples-workitems/{blobName}",
                dataType = "binary")
                byte[] content,
            @BindingName("blobName") String blobName,
            @BlobOutput(
                connection = "MY_STORAGE_OUT",
                name = "outputblob",
                path = "samples-workitems/{blobName}")
                OutputBinding<String> outputData,
            final ExecutionContext context) {

        context.getLogger().info("Java Blob trigger function processed a blob.\nBlob Name: "
                                + sourceContainerName + "/" + blobName + "\nBlob Size: " + content.length + " Bytes");

        // Get Storage Connection String of the source storage account
        String sourceStorageConnectionString = System.getenv("MY_STORAGE_IN");
        
        try {
            // Copy the blob
            context.getLogger().info("Moving blob: " + blobName);
            
            // Save blob to outputData
            outputData.setValue(new String(content, StandardCharsets.UTF_8));

            // Delete the source Blob
            deleteBlob(sourceStorageConnectionString, sourceContainerName, blobName, context);

            context.getLogger().info("Moved");
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }

    /**
     * Delete a Blob in a Blob Storage container.
     */
    private static void deleteBlob(String storageConnectionString, String containerName, String blobName,
                                    ExecutionContext context) {
        try {

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
                } else {
                    context.getLogger().info("Error: Blob \"%s\" does NOT exist.\n" + blobName);
                }
            } else {
                context.getLogger().info("Error: Blob Storage container \"%s\" does NOT exist.\n" + containerName);
            }
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
