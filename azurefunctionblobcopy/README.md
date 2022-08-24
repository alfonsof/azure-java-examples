# Azure Function Blob Storage copy Java example

This folder contains a Java application example that handles Functions on Microsoft Azure.

It handles an Azure Function that responds to a Blob Storage event (trigger) and copy the blob when it appears in a blob storage to another blob storage.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* To develop functions app with Java, you must have the following installed:
  * Java Developer Kit, version 8.
  * Apache Maven, version 3.0 or above.
  * Azure CLI
  * Azure Functions Core Tools Version 3.x

* The code was written for:
  * Java 8
  * Apache Maven 3

* Azure Functions Core Tools Version 3.x

  Azure Functions Core Tools lets you develop and test your functions on your local computer from the command prompt or terminal. Your local functions can connect to live Azure services, and you can debug your functions on your local computer using the full Functions runtime. You can even deploy a function app to your Azure subscription.

  Version 3.x/2.x: Supports either version 3.x or 2.x of the Azure Functions runtime. These versions support Windows, macOS, and Linux and use platform-specific package managers or npm for installation.

  Azure Functions Core Tools currently depends on either the Azure CLI or Azure PowerShell for authenticating with your Azure account. This means that you must install one of these tools to be able to publish to Azure from Azure Functions Core Tools.

  Version 3.x/2.x of the tools uses the Azure Functions runtime that is built on .NET Core. This version is supported on all platforms .NET Core supports, including Windows, macOS, and Linux.

  Install version 3.x of the Core Tools on your local computer:
  
  * For Windows:

    1. Download and run the Core Tools installer, based on your version of Windows:

        * v3.x - Windows 64-bit (Recommended. Visual Studio Code debugging requires 64-bit.)
        * v3.x - Windows 32-bit

    2. If you don't plan to use extension bundles, install the .NET Core 3.x SDK for Windows.

  * For MacOS:

    1. Install Homebrew, if it's not already installed.
    2. Install the Core Tools package:

       ```bash
       brew tap azure/functions
       brew install azure-functions-core-tools@3
       # if upgrading on a machine that has 2.x installed
       brew link --overwrite azure-functions-core-tools@3
       ```

    3. If you don't plan to use extension bundles, install the .NET Core 3.x SDK for macOS.

## Using the code

* Create the Azure Funtion project and the Azure Function (Boilerplate code)

  *This step is only necessary when you want to create an Azure Function from scratch.*

  The Azure Functions Core Tools help you to create the boilerplate code for the Azure Funtion project and the Azure Function using a Maven archetype

  In the terminal window or from a command prompt, navigate to an empty folder for your project, and run the following command:

  ```bash
  mvn archetype:generate "-DarchetypeGroupId=com.microsoft.azure" "-DarchetypeArtifactId=azure-functions-archetype" "-DjavaVersion=8" "-Dtrigger=BlobTrigger"
  ```

  Maven asks you for values needed to finish generating the project on deployment. Provide the following values when prompted:
  * groupId: com.alfonsof.azureexamples
  * artifactId: azurefunctionblobcopy
  * version: 2.0.0
  * package: example
  * trigger: BlobTrigger

  Then, the project is created with `azurefunctionblobcopy` and these files:

  * `host.json` - JSON configuration file.
  * `local.settings.json` - It stores app settings, connection strings, and settings used by local development tools. Settings in the local.settings.json file are used only when you're running projects locally.
  * `pom.xml` - Maven POM file.
  * The `src/main/java/example` folder content is:
    * `Function.java` - Code of the function.

  *Because `local.settings.json` can contain secrets downloaded from Azure, the file is excluded from source control by default in the `.gitignore` file.*

* Create a Storage Account for the input source
  
  You must create the Storage Account, using the Azure console.
  The storage account must be a StorageV2 (general purpose v2) account kind.
  Create a blob container with the `samples-workitems` name in this Storage Account.

* Create a Storage Account for the ouput target
  
  You must create the Storage Account, using the Azure console.
  The storage account must be a StorageV2 (general purpose v2) account kind.
  Create a blob container with the `samples-workitems` name in this Storage Account.

* Configure the Azure Function.

  1. You must configurate the `pom.xml` file for a proper creation of the Function App during the subsequent deployment process.

      Replace with the proper values in the `pom.xml` file:

      * `<FUNCTION_APP>` - Function App name.
      * `<RESOURCE_GROUP>` - Resource group name.
      * `<APP_SERVICE_PLAN>` - App Service Plan name.
      * `<REGION>`- Azure region name.
      * `<os>linux</os>` - It is for a Linux operating system.
      * `<javaVersion>8</javaVersion>` - It is for a runtime Java version 8.
      * `<value>~3</value>` - It is for functions version 3.

      Part of the `pom.xml` file:

      ```bash
      <configuration>
        <!-- function app name -->
        <appName><FUNCTION_APP></appName>
        <!-- function app resource group -->
        <resourceGroup><RESOURCE_GROUP></resourceGroup>
        <!-- function app service plan name -->
        <appServicePlanName><APP_SERVICE_PLAN></appServicePlanName>
        <!-- function app region-->
        <region><REGION></region>
        <runtime>
            <!-- runtime os, could be windows, linux or docker-->
            <os>linux</os>
            <javaVersion>8</javaVersion>
        </runtime>
        <appSettings>
            <property>
                <name>FUNCTIONS_EXTENSION_VERSION</name>
                <value>~3</value>
            </property>
        </appSettings>
      </configuration>
      ```

  2. You must configure the connection string for trigger in the `local.settings.json` file when running locally.
  
      You must define the `AzureWebJobsStorage` variable in the `local.settings.json` file:

      ```bash
      "AzureWebJobsStorage": "DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_NAME>;AccountKey=<STORAGE_ACCOUNT_KEY>;EndpointSuffix=core.windows.net",
      ```

      Replace with the proper:

      * `<STORAGE_ACCOUNT_NAME>` - Name of the Storage Account.
      * `<STORAGE_ACCOUNT_KEY>` - Key of the Storage Account.

      You must define the `MY_STORAGE_IN` and `MY_STORAGE_OUT` variables in the `local.settings.json` file:

      ```bash
      "MY_STORAGE_IN": "DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_IN>;AccountKey=<ACCOUNT_KEY_IN>;EndpointSuffix=core.windows.net",
      "MY_STORAGE_OUT": "DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_OUT>;AccountKey=<ACCOUNT_KEY_OUT>;EndpointSuffix=core.windows.net"
      ```

      Replace with the proper:

      * `<STORAGE_ACCOUNT_IN>` - Storage Account name for input source.
      * `<ACCOUNT_KEY_IN>` - Account Key of the Storage Account for input source.
      * `<STORAGE_ACCOUNT_OUT>`- Storage Account name for output target.
      * `<ACCOUNT_KEY_OUT>` - Account Key of the Storage Account for output target.

* Package the function

  Package your code into a new Function app using the `package` Maven target.

  ```bash
  mvn clean package
  ```

* Run your function project locally

  You can run your function locally.
  
  Enter the following command to run your function app:

  ```bash
  mvn azure-functions:run
  ```

  The runtime will waiting for a blob event (trigger).

  Upload a file to the source blob storage container in the storage account.

  The file from the source blob storage container should be copied to the target blob storage container.

  You should see the next message in the log:
  
  ```bash
  Java Blob trigger function processed a blob.
  Blob Name: samples-workitems/<FILE_NAME>
  Blob Size: <XX> Bytes
  Copying blob: <FILE_NAME>
  Copied
  ```

  To stop debugging, use Ctrl-C in the terminal.

* Deploy the function to Azure & Create the Function App

  The deploy process to Azure Functions uses account credentials from the Azure CLI. Log in with the Azure CLI before continuing.

  ```bash
  az login
  ```

  Deploy your code into a new Function app using the `azure-functions:deploy` Maven target.

  ```bash
  mvn azure-functions:deploy
  ```

  This creates the Function App using the configuration in the `pom.xml` file, and the Storage Account that Function App needs. In addition, the function is deployed to the Function App.

  When the deploy is complete, you will see the message:

  ```bash
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time:  42.197 s
  [INFO] Finished at: 2022-08-24T16:27:06+02:00
  [INFO] ------------------------------------------------------------------------
  ```

* Configure the connection string for trigger Blob in the Function App.

  You must configure the connection strings or secrets for trigger, input map to values in the application settings for the Function App when running in Azure.

  You can make that in two ways:

  * Using the Azure console.

    Go to your Function App.

    Select: Settings > Configuration > Application settings

    Set the setting `MY_STORAGE_IN` name to:
    `DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_IN>;AccountKey=<ACCOUNT_KEY_IN>;EndpointSuffix=core.windows.net`

    Set the setting `MY_STORAGE_OUT` name to:
    `DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_OUT>;AccountKey=<ACCOUNT_KEY_OUT>;EndpointSuffix=core.windows.net`

  * Using the Azure CLI

    ```bash
    az functionapp config appsettings set --name MyFunctionApp --resource-group MyResourceGroup --settings "MY_STORAGE_IN=DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_IN>;AccountKey=<ACCOUNT_KEY_IN>;EndpointSuffix=core.windows.net"
    ```

    ```bash
    az functionapp config appsettings set --name MyFunctionApp --resource-group MyResourceGroup --settings "MY_STORAGE_OUT=DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_OUT>;AccountKey=<ACCOUNT_KEY_OUT>;EndpointSuffix=core.windows.net"
    ```

  In both cases, replace with the proper:

  * `<STORAGE_ACCOUNT_IN>`- Storage Account name for input source.
  * `<ACCOUNT_KEY_IN>` - Account Key of the Storage Account for input source.
  * `<STORAGE_ACCOUNT_OUT>`- Storage Account name for output target.
  * `<ACCOUNT_KEY_OUT>` - Account Key of the Storage Account for output target.

* Test the function

  Upload a file to the source blob storage container in the storage account.

  The file from the source blob storage container should be copied to the target blob storage container.

  You should see the next message in the log:
  
  ```bash
  Java Blob trigger function processed a blob.
  Blob Name: samples-workitems/<FILE_NAME>
  Blob Size: <XX> Bytes
  Copying blob: <FILE_NAME>
  Copied
  ```
