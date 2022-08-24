package example;

import java.nio.charset.StandardCharsets;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobOutput;


/**
 * Azure Functions with Azure Blob trigger and copy a Blob.
 */
public class Function {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path.
     * The blob contents are provided as input to this function.
     * Copy the blob when it appears in a blob storage to another blob storage.
     */

    @FunctionName("BlobCopy")
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
                                + "samples-workitems/" + blobName + "\nBlob Size: " + content.length + " Bytes");

        try {
            // Copy the blob
            context.getLogger().info("Copying blob: " + blobName);
            
            // Save blob to outputData
            outputData.setValue(new String(content, StandardCharsets.UTF_8));

            context.getLogger().info("Copied");
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
