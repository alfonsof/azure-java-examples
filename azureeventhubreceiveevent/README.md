# Azure Event Hub receive event Java example

This folder contains a Java application example that handles Event Hubs on Microsoft Azure.

It handles an Event Hub and receive events from an event hub event stream.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* The code was written for:
  * Java 8
  * Apache Maven 3
  * Azure SDK for Java: New Client Libraries (Azure Event Hub library v5)

## Using the code

* Configure your Azure access.

  You must create an Azure AD service principal in order to enable application to connect resources into Azure. The service principal grants your application to manage resources in your Azure subscription.

  The Azure SDKs Libraries for Java allow you to use several authentication schemes.

  The application uses an authentication file for authenticating.

  The credentials are taken from `AZURE_AUTH_LOCATION` environment variable.

  You can create a service principal and generate this file using Azure CLI 2.0 or using the Azure cloud shell.

  * Make sure you select your subscription by:

    ```bash
    az account set --subscription <name or id>
    ```

    and you have the privileges to create service principals.

  * Execute the following command for creating the service principal and the authentication file:
  
    ```bash
    az ad sp create-for-rbac --sdk-auth > my.azureauth
    ```
  
  * Set the `AZURE_AUTH_LOCATION` environment variable in your Operating System with the path of your authentication file.

    ```bash
    AZURE_AUTH_LOCATION = /path/to/my.azureauth
    ```

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

* Create an Azure storage account and a blob container.

  Create an Azure storage account and a blob container in it by doing the following steps, using the Azure console:

  1. Create an Azure Storage account.

  2. Create a blob container.

  3. Get the connection string to the storage account.

* Configure your application.

  The configuration is stored in the `app.properties` properties file, located in the path `src/main/resources`. The file content is:

  ```bash
  StorageAccountConnectionString=<STORAGE_ACCOUNT_CONNECTION_STRING>
  StorageAccountContainerName=<STORAGE_ACCOUNT_CONTAINER_NAME>
  EventHubConnectionString=<EVENT_HUB_CONNECTION_STRING>
  EventHubName=<EVENT_HUB_NAME>
  ```

  You must edit the file `app.properties` and replace the values of:
  
  * `<STORAGE_ACCOUNT_CONNECTION_STRING>` by the Connection string for the Storage Account.
  * `<STORAGE_ACCOUNT_CONTAINER_NAME>` by the Blob name in the Storage Account.
  * `<EVENT_HUB_CONNECTION_STRING>` by the Connection string for the Event Hub.
  * `<EVENT_HUB_NAME>` by the name of the Event Hub.
  
  The application uses this information for accessing your Event Hub and Storage Account.

* Run the code.

  Execute the receiver application:

  ```bash
  java -jar azureeventhubreceiveevent.jar
  ```

  You should see the next message:
  
  ```bash
  Starting event processor ...
  Waiting for an event ...
  Press enter to stop.
  ```

  The application is waiting for some event from the Event Hub.

* Test the application.

  You must send an event to your Event Hub.

  You can use the Java application `azureeventhubsendevent.jar` (Event Hub send event). You can get it following this link: [../azureeventhubsendevent/](../azureeventhubsendevent)

  In another command line console, execute the sender application:

  ```bash
  java -jar azureeventhubsendevent.jar
  ```

  You should see the next message:
  
  ```bash
  Preparing batch of events ...
  Sending batch of events to Event Hub ...
  Sent
  ```

  When the receiver application gets the events, you should see the next message in you receiver application:
  
  ```bash
  Processing event from partition <X> with sequence number <X> with body: First event
  Processing event from partition <X> with sequence number <X> with body: Second event
  Processing event from partition <X> with sequence number <X> with body: Third event
  ```
