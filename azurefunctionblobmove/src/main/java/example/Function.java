package example;

import java.io.IOException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;


/**
 * Azure Functions with Azure Blob trigger.
 */
public class Function {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     * Move the blob when it appears in a blob storage to another blob storage.
     */
    @FunctionName("Function")
    @StorageAccount("<connection>")
    public void run(
        @BlobTrigger(connection = "MY_STORAGE_IN", name = "content", path = "samples-workitems/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Blob trigger function processed a blob.\nBlob Name: samples-workitems/" + name + "\nBlob Size: " + content.length + " Bytes");
        
        CloudStorageAccount sourceStorageAccount;
        CloudBlobClient sourceBlobClient = null;
        CloudBlobContainer sourceContainer = null;
        CloudBlob sourceBlob = null;

        CloudStorageAccount destinationStorageAccount;
        CloudBlobClient destinationBlobClient = null;
        CloudBlobContainer destinationContainer = null;
        CloudBlob destinationBlob = null;
        
        // Get Storage Connection String of thesource storage account
        String sourceStorageConnectionString = System.getenv("MY_STORAGE_IN");

        // Get Storage Connection String of the destination storage account
        String destinationStorageConnectionString = System.getenv("MY_STORAGE_OUT");

        try {
            // Parse the connection string and create a blob client to interact with source Blob storage
            sourceStorageAccount = CloudStorageAccount.parse(sourceStorageConnectionString);
            sourceBlobClient = sourceStorageAccount.createCloudBlobClient();
            sourceContainer = sourceBlobClient.getContainerReference("samples-workitems");
            sourceBlob = sourceContainer.getBlockBlobReference(name);

            // Parse the connection string and create a blob client to interact with destination Blob storage
            destinationStorageAccount = CloudStorageAccount.parse(destinationStorageConnectionString);
            destinationBlobClient = destinationStorageAccount.createCloudBlobClient();
            destinationContainer = destinationBlobClient.getContainerReference("samples-workitems");
            destinationBlob = destinationContainer.getBlockBlobReference(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Copy the blob
            context.getLogger().info("Moving blob: " + name);
            destinationBlob.uploadFromByteArray(content, 0, content.length);
            // Delete the source blob
            sourceBlob.deleteIfExists();
            context.getLogger().info("Moved");
        } catch (StorageException se) {
            se.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
