# Azure Function HTTP Request Java example

This folder contains a Java application example that handles Functions on Microsoft Azure.

It handles an Azure Function that responds to an HTTP request.

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
  mvn archetype:generate "-DarchetypeGroupId=com.microsoft.azure" "-DarchetypeArtifactId=azure-functions-archetype" "-DjavaVersion=8"
  ```

  Maven asks you for values needed to finish generating the project on deployment. Provide the following values when prompted:
  * groupId: com.alfonsof.azureexamples
  * artifactId: azurefunctionhttprequest
  * version: 1.0.0
  * package: example
  * trigger: HttpTrigger

  Then, the project is created with `azurefunctionhttprequest` and these files:

  * `host.json` - JSON configuration file.
  * `local.settings.json` - It stores app settings, connection strings, and settings used by local development tools. Settings in the local.settings.json file are used only when you're running projects locally.
  * `pom.xml` - Maven POM file.
  * The `src/main/java/example` folder content is:
    * `Function.java` - Code of the function.
  * The `src/test/java/example` folder content is:
    * `FunctionTest.java` - A unit test for your function.
    * `HttpResponseMessageMock.java` - The mock for HttpResponseMessage.

  *Because `local.settings.json` can contain secrets downloaded from Azure, the file is excluded from source control by default in the `.gitignore` file.*

* Configure the Azure Function.

  You must configurate the `pom.xml` file for a proper creation of the Function App during the subsequent deployment process.

  Select in the `pom.xml` file the proper:

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

* Package the Azure Function.

  Package your code into a new Function app using the `package` Maven target.

  ```bash
  mvn clean package
  ```

* Run your function project locally.

  You can run your function locally.
  
  Enter the following command to run your function app:

  ```bash
  mvn azure-functions:run
  ```

  The runtime will output a URL for any HTTP functions, which can be copied and run in your browser's address bar.

  ```bash
  http://localhost:7071/api/HttpExample
  ```

  And you have to add the parameter `name`:

  ```bash
  http://localhost:7071/api/HttpExample?name=Peter
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

  When the deploy is complete, you see the URL you can use to access your Azure function app:

  ```bash
  [INFO] HTTP Trigger Urls:
  [INFO]   datahub01httpeampleafb/HttpExample : https://functionapphttpexample.azurewebsites.net/api/httpexample
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time:  03:12 min
  [INFO] Finished at: 2021-08-05T16:31:04+02:00
  [INFO] ------------------------------------------------------------------------
  ```

* Run the code.

  You can use the url that you got before, or using the Azure console, you can get:
  
  * The function URL: URL field

  * The function key: Functions menu > select function `HttpExample` > Function keys > `default` value

  Run the function:

  Your HTTP request normally looks like the following URL:

  ```bash
  https://<functionapp>.azurewebsites.net/api/<function>
  ```

  To run the code, you need to use the parementer `name`:

  ```bash
  https://<FUNCTION_APP>.azurewebsites.net/api/httpexample?name=PETER
  ```

* Test the function.

  Go to the URL: `https://<FUNCTION_APP>.azurewebsites.net/api/httpexample?name=PETER` using a browser.

  You should see the response:

    ```bash
    Hello, PETER
    ```
