# Azure Virtual Machines Java example

This folder contains a Java application example that handles Virtual Machines on Microsoft Azure.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* You must have the following installed:
  * Java Development Kit (JDK) 8
  * Apache Maven 3
  * Azure CLI

* The code was written for:
  * Java 8
  * Apache Maven 3
  * Azure SDK for Java: New Management Libraries (com.azure)

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

* Configure your AZURE_SUBSCRIPTION_ID environment variable.

  Set the `AZURE_SUBSCRIPTION_ID` environment variable with the Microsoft Azure subscription ID.

  To set this variable on Linux, macOS, or Unix, use `export`:

  ```bash
  export AZURE_SUBSCRIPTION_ID=<SUBSCRIPTION_ID>
  ```

  To set this variable on Windows, use `set`:

  ```bash
  set AZURE_SUBSCRIPTION_ID=<SUBSCRIPTION_ID>
  ```

  You must replace the value of:

  * `<SUBSCRIPTION_ID>` by the Microsoft Azure subscription ID (Ex.: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx").

* Run the code.

  Run application:

  ```bash
  java -jar azurevm.jar
  ```

  You can select an option in the menu in order to run every command:

  * 1 = List all Virtual Machines
  * 2 = Create a Virtual Machine
  * 3 = List Virtual Machine
  * 4 = Start Virtual Machine
  * 5 = Stop Virtual Machine
  * 6 = Restart Virtual Machine
  * 7 = Delete/Deallocate Virtual Machine

* Test the application.

  You should see the new virtual machine and modification of states with the Azure console.
  