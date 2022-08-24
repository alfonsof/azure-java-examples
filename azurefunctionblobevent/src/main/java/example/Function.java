package example;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.BindingName;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class Function {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path.
     * The blob contents are provided as input to this function.
     */
    @FunctionName("BlobEvent")
    public void run(
            @BlobTrigger(
                connection = "MY_STORAGE_IN",
                name = "inputblob",
                path = "samples-workitems/{blobName}",
                dataType = "binary")
                byte[] content,
            @BindingName("blobName") String blobName,
            final ExecutionContext context) {

        context.getLogger().info("Java Blob trigger function processed a blob.\nBlob Name: samples-workitems/"
                                    + blobName + "\nBlob Size: " + content.length + " Bytes");
    }
}
