# Azure Event Grid event Java example

This folder contains a Java application example that handles Event Grids on Microsoft Azure.

It handles an Event Grid and sends events to an Event Grid Topic.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* You must have the following installed:
  * Java Development Kit (JDK) 8
  * Apache Maven 3 
  * Azure CLI

* The code was written for:
  * Java 8
  * Apache Maven 3
  * Azure SDK for Java: New Client Libraries (Azure Event Grid library v4) (com.azure)

## Using the code

* Sign in Azure (Interactively).

  The Azure CLI's default authentication method for logins uses a web browser and access token to sign in.

  1. Run the Azure CLI login command.

      ```bash
      az login
      ```

      If the CLI can open your default browser, it will do so and load an Azure sign-in page.

      Otherwise, open a browser page at https://aka.ms/devicelogin and enter the authorization code displayed in your terminal.

      If no web browser is available or the web browser fails to open, use device code flow with az login --use-device-code.

  2. Sign in with your account credentials in the browser.

  Make sure you select your subscription by:

  ```bash
  az account set --subscription <name or id>
  ```

* Create an Event Grid Topic.

  An Event Grid topic provides a user-defined endpoint that you post your events to.

  You must create the Event Grid Topic, using the Azure console, do the following actions:

  1. Select `Create a resource` and chose `Event Grid Topic`.

  2. On the Event Grid Topics page, select `Create`.

  3. Choose the `Subscription`, `Resource group`, `Name` and `Region` for your Event Grid Topic.

  4. Select `Create`.

  You need the `Topic Key` and the `Topic Endpoint`. You can find these within the Event Grid Topic resource on the Azure portal.

* Configure your application.

  The configuration is stored in the `app.properties` properties file, located in the path `src/main/resources`. The file content is:

  ```bash
  EventGridTopicKey=<EVENT_GRID_TOPIC_KEY>
  EventGridTopicEndpoint=<EVENT_GRID_TOPIC_ENDPOINT>
  ```

  You must edit the file `app.properties` and replace the values of:
  
  * `<EVENT_GRID_TOPIC_KEY>` by the key of the Event Grid Topic.
  * `<EVENT_GRID_TOPIC_ENDPOINT>` by the topic endpoint in the Event Grid.
  
  The application uses this information for accessing your Event Grid Topic.

* Run the code.

  Execute the java application:

  ```bash
  java -jar azureeventgridsendevent.jar
  ```

  You should see the next message:
  
  ```bash
  Preparing batch of events ...
  Sending batch of events to Event Grid ...
  Sent
  ```

* Test the application.

  The events should be in the Event Grid.
  