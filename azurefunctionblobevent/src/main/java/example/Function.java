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
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("Function")
    @StorageAccount("<connection>")
    public void run(
        @BlobTrigger(connection = "MY_STORAGE_IN", name = "content", path = "samples-workitems/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Blob trigger function processed a blob.\nBlob Name: samples-workitems/" + name + "\nBlob Size: " + content.length + " Bytes");
    }
}
