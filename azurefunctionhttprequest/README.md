# Azure Function HTTP Request Java example

This folder contains a Java application example that handles Functions on Microsoft Azure.

It handles an Azure Function that responds to an HTTP request.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* The code was written for Java 8.

* To develop functions app with Java, you must have the following installed:
  * Java Developer Kit, version 8.
  * Apache Maven, version 3.0 or above.
  * Azure CLI

* Azure Functions Core Tools Version 2.x

  The Azure Functions Core Tools provide a local development environment for writing, running, and debugging Azure Functions from the terminal or command prompt.

  Version 2.x of the tools uses the Azure Functions runtime 2.x that is built on .NET Core. This version is supported on all platforms .NET Core 2.x supports, including Windows, macOS, and Linux.

  Install version 2 of the Core Tools on your local computer:
  
  * For Windows:

    1. Install .NET Core 2.1 for Windows
    2. Install Node.js, which includes npm. For version 2.x of the tools, only Node.js 8.5 and later versions are supported.
    3. Install the Core Tools package:
      ```bash
      npm install -g azure-functions-core-tools
      ```

  * For MacOS:

    1. Install .NET Core 2.1 for MacOS
    2. Install Homebrew, if it's not already installed.
    3. Install the Core Tools package:
      ```bash
         brew tap azure/functions
         brew install azure-functions-core-tools 
      ```

## Using the code

* Create an Azure Functions project
  
  * Using Maven:

    In the terminal window or from a command prompt, navigate to an empty folder for your project, and run the following command:

    ```bash
    mvn archetype:generate -DarchetypeGroupId=com.microsoft.azure -DarchetypeArtifactId=azure-functions-archetype -DappName=MyFunction11 -DappRegion={region} -DresourceGroup={resourceGroup} -DgroupId=com.{functionAppName}.group -DartifactId={functionAppName}-functions -Dpackage=com.{functionAppName} -DinteractiveMode=false
    ```

  * Using IntelliJ:

    1. In IntelliJ IDEA, select `Create New Project`.
    2. In the New Project window, select `Maven` from the left pane.
    3. Select the `Create from archetype` check box, and then select `Add Archetype` for the `azure-functions-archetype`.
    4. In the Add `Archetype window`, complete the fields as follows:
        GroupId: `com.microsoft.azure`
        ArtifactId: `azure-functions-archetype`
        Version: Use the latest version from the central repository `https://mvnrepository.com/artifact/com.microsoft.azure/azure-functions-archetype`
    5. Select OK, and then select Next.
    6. Enter your details for current project, and select Finish.

* Package the function

  Package your code into a new Function app using the `package` Maven target.

  ```bash
  mvn package
  ```

* Run your function project locally

  You can run your function locally.
  
  Enter the following command to run your function app:

  ```bash
  mvn clean package
  mvn azure-functions:run
  ```

  The runtime will output a URL for any HTTP functions, which can be copied and run in your browser's address bar.

  To stop debugging, use Ctrl-C in the terminal.

* Deploy the function to Azure

  The deploy process to Azure Functions uses account credentials from the Azure CLI. Log in with the Azure CLI before continuing.

  ```bash
  az login
  ```

  Deploy your code into a new Function app using the `azure-functions:deploy` Maven target.

  ```bash
  mvn azure-functions:deploy
  ```

  When the deploy is complete, you see the URL you can use to access your Azure function app:

  ```bash
  [INFO] Successfully deployed Function App with package.
  [INFO] Deleting deployment package from Azure Storage...
  [INFO] Successfully deleted deployment package fabrikam-function-20170920120101928.20170920143621915.zip
  [INFO] Successfully deployed Function App at https://fabrikam-function-20170920120101928.azurewebsites.net
  [INFO] ------------------------------------------------------------------------
  ```

* Run the code.

  Using the Azure console, get the function URL:

  * Select `Function Apps`
  * Select `azure-function-http-request`
  * Select your function: `HttpTrigger-Java`
  * Select `</> Get function URL`
  * You will get your URL with Key: `default (Function key)`

  Run function:

  Your HTTP request normally looks like the following URL:

  ```bash
  https://<functionapp>.azurewebsites.net/api/<function>?code=<ApiKey>
  ```

  To run the code, you need to use the parementer `name`:

  ```bash
  https://<FUNCTION_APP>.azurewebsites.net/api/HttpTrigger-Java?name=PETER&code=<FUNCTION_KEY>
  ```

* Test the function.

  Go to the URL: `https://<YOUR_APP>.azurewebsites.net/api/HttpTrigger-Java?name=PETER&code=<FUNCTION_KEY>` using a browser.

  You should see the response:

    ```bash
    Hello, PETER
    ```
