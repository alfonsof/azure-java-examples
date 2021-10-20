# Azure Function Event Hub event Java example

This folder contains a Java application example that handles Functions on Microsoft Azure.

It handles an Azure Function that responds to an Event Hub event (trigger) when an event is sent to an event hub event stream.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* The code was written for:
  * Java 8

* To develop functions app with Java, you must have the following installed:
  * Java Developer Kit, version 8.
  * Apache Maven, version 3.0 or above.
  * Azure CLI
  * Azure Functions Core Tools Version 3.x

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

* Create the Azure Funtion project and the Azure Function (Boilerplate code).

  *This step is only necessary when you want to create an Azure Function from scratch.*

  The Azure Functions Core Tools help you to create the boilerplate code for the Azure Funtion project and the Azure Function using a Maven archetype

  In the terminal window or from a command prompt, navigate to an empty folder for your project, and run the following command:

  ```bash
  mvn archetype:generate "-DarchetypeGroupId=com.microsoft.azure" "-DarchetypeArtifactId=azure-functions-archetype" "-DjavaVersion=8" "-Dtrigger=EventHubTrigger"
  ```

  Maven asks you for values needed to finish generating the project on deployment. Provide the following values when prompted:
  * groupId: com.alfonsof.azureexamples
  * artifactId: azurefunctioneventhubevent
  * version: 1.0.0
  * package: example
  * trigger: EventHubTrigger

  Then, the project is created with `azurefunctioneventhubevent` and these files:

  * `host.json` - JSON configuration file.
  * `local.settings.json` - It stores app settings, connection strings, and settings used by local development tools. Settings in the local.settings.json file are used only when you're running projects locally.
  * `pom.xml` - Maven POM file.
  * The `src/main/java/example` folder content is:
    * `Function.java` - Code of the function.

  *Because `local.settings.json` can contain secrets downloaded from Azure, the file is excluded from source control by default in the `.gitignore` file.*

* Create an Event Hubs Namespace and an Event Hub.

  1. Create an Event Hubs Namespace.

     An Event Hubs namespace provides a unique scoping container, in which you create one or more event hubs.

     To create a namespace in your resource group using the portal, do the following actions:

     1. You must create the Event Hubs Namespace, using the Azure console.

     2. Select the your data for: Suscription, Resource group, Namespace name and Location.

     3. Choose Basic for the pricing tier.

  2. Create an Event Hub.

     You must create the Event Hub, using the Azure console.

     To create an event hub within the namespace, do the following actions:

     1. On the Event Hubs Namespace page, select `Event Hubs` in the left menu.

     2. At the top of the window, select `+ Event Hub`.

     3. Type a name for your event hub, then select `Create`.

  3. Create a SAS Policy.

     You must create the SAS Policy, using the Azure console.

     1. On the Event Hubs page for the Event Hub created, select `Shared access policies` in the left menu.

     2. At the top of the window, select `+ Add`.

     3. Type a name for your Policy, select `Manage`, that includes `Send` and `Listen`, then select `Create`.

* Configure the Azure Function.

   1. You must configurate the `pom.xml` file:

      You must configurate the `pom.xml` file for a proper creation of the Function App during the subsequent deployment process.

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

  2. You must configurate the `Funtion.java` file:

      * Defining the `connection` with a variable `MY_EVENT_HUB_IN` for the in binding:

        ```bash
        connection = "MY_EVENT_HUB_IN"
        ```

      The variable `name`, in the `Funtion.java` file, will hold the parameter that receives the event item.

  3. You must configure the connection strings or secrets for trigger, input map to values in:
  
      * The `local.settings.json` file when running locally.

        You must define the `MY_EVENT_HUB_IN` variable in the `local.settings.json` file:

        ```bash
        "MY_EVENT_HUB_IN": "Endpoint=sb://<EVENT_HUB_NAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<EVENT_HUB_SAS_POLICY>;SharedAccessKey=<EVENT_HUB_KEY>;EntityPath=<EVENT_HUB_NAME>",
        ```

        Replace with the proper:

        * `<EVENT_HUB_NAMESPACE>` - Event Hub namespace.
        * `<EVENT_HUB_SAS_POLICY>` - Event Hub SAS Policy.
        * `<EVENT_HUB_KEY>` - Key of the Event Hub.
        * `<EVENT_HUB_NAME>` - Name of the Event Hub.

        You must define the `AzureWebJobsStorage` variable in the `local.settings.json` file:

        ```bash
        "AzureWebJobsStorage": "DefaultEndpointsProtocol=https;AccountName=<STORAGE_ACCOUNT_NAME>;AccountKey=<STORAGE_ACCOUNT_KEY>;EndpointSuffix=core.windows.net",
        ```

        Replace with the proper:

        * `<STORAGE_ACCOUNT_NAME>` - Name of the Storage Account.
        * `<STORAGE_ACCOUNT_KEY>` - Key of the Storage Account.

      * The application settings for the Function App when running in Azure.

        You can make that in two ways:

        * Using the Azure console.

          Go to your Function App.

          Select: `Settings > Configuration > Application settings > + New application setting`

          Set the setting `MY_EVENT_HUB_IN` name to:
  
          `Endpoint=sb://<EVENT_HUB_NAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<EVENT_HUB_SAS_POLICY>;SharedAccessKey=<EVENT_HUB_KEY>`

          Select `Save`.

        * Using the Azure CLI.

          ```bash
          az functionapp config appsettings set --name MyFunctionApp --resource-group MyResourceGroup --settings "MY_EVENT_HUB_IN=Endpoint=sb://<EVENT_HUB_NAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<EVENT_HUB_SAS_POLICY>;SharedAccessKey=<EVENT_HUB_KEY>;EntityPath=<EVENT_HUB_NAME>""
          ```

        In both cases, replace with the proper:

        * `<EVENT_HUB_NAMESPACE>` - Event Hub namespace.
        * `<EVENT_HUB_SAS_POLICY>` - Event Hub SAS Policy.
        * `<EVENT_HUB_KEY>` - Key of the Event Hub.
        * `<EVENT_HUB_NAME>` - Name of the Event Hub.

* Package the Azure Function.

  Package your code into a new Function app using the `package` Maven target.

  ```bash
  mvn clean package
  ```

* Run your Azure Function project locally.

  You can run your function locally.
  
  Enter the following command to run your function app:

  ```bash
  mvn azure-functions:run
  ```

  The runtime will waiting for a Event Hub event (trigger).

  You must send an event to your Event Hub.

  You can use the Python application `azureeventhubsendevent.jar` (Event Hub send event). You can get it following this link: [../azureeventhubsendevent/](../azureeventhubsendevent)

  Execute the python application:

  ```bash
  java -jar azureeventhubsendevent.jar
  ```

  You should see the next message in the log:
  
  ```bash
  Java Event Hub trigger function executed.
  Length: <XXXX>
  <XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>
  ```

  To stop debugging, use Ctrl-C in the terminal.

* Deploy the Azure Function to Azure & Create the Function App.

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
  [INFO] Total time:  01:38 min
  [INFO] Finished at: 2021-10-11T19:46:39+02:00
  [INFO] ------------------------------------------------------------------------
  ```

  * Configure in the Function App the connection string for trigger Event Hub.

    You must configure the connection strings or secrets for trigger, input map to values in the application settings for the Function App when running in Azure.

    You can make that in two ways:

    * Using the Azure console.

      Go to your Function App.

      Select: `Settings > Configuration > Application settings > + New application setting`

      Set the setting `MY_EVENT_HUB_IN` name to:

      `Endpoint=sb://<EVENT_HUB_NAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<EVENT_HUB_SAS_POLICY>;SharedAccessKey=<EVENT_HUB_KEY>`

      Select `Save`.

    * Using the Azure CLI.

      ```bash
      az functionapp config appsettings set --name MyFunctionApp --resource-group MyResourceGroup --settings "MY_EVENT_HUB_IN=Endpoint=sb://<EVENT_HUB_NAMESPACE>.servicebus.windows.net/;SharedAccessKeyName=<EVENT_HUB_SAS_POLICY>;SharedAccessKey=<EVENT_HUB_KEY>;EntityPath=<EVENT_HUB_NAME>""
      ```

    In both cases, replace with the proper:

    * `<EVENT_HUB_NAMESPACE>` - Event Hub namespace.
    * `<EVENT_HUB_SAS_POLICY>` - Event Hub SAS Policy.
    * `<EVENT_HUB_KEY>` - Key of the Event Hub.
    * `<EVENT_HUB_NAME>` - Name of the Event Hub.

* Test the function.

  You must send an event to your Event Hub.

  You can use the Python application `azureeventhubsendevent.jar` (Event Hub send event). You can get it following this link: [../azureeventhubsendevent/](../azureeventhubsendevent)

  Execute the python application:

  ```bash
  java -jar azureeventhubsendevent.jar
  ```

  You should see the next message in the log:
  
  ```bash
  Java Event Hub trigger function executed.
  Length: <XXXX>
  <XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>
  ```
