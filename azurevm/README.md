# Azure Virtual Machines Java example

This folder contains a Java application example that handles Virtual Machines on Microsoft Azure.




## Requirements

You must have a [Microsoft Azure subscription](https://azure.microsoft.com/).

This code was written for Java 1.8 and Azure Management Libraries for Java.




## Using the code

* Configure your Azure access.

  The Azure Management Libraries for Java allow you to use several authentication schemes.

  The application uses an authentication file for authenticating.

  The credentials are taken from AZURE_AUTH_LOCATION environment variable.

  For example:
  
  ```
  AZURE_AUTH_LOCATION = /path/to/my.azureauth
  ```

  You can generate this file using Azure CLI 2.0 or using the Azure cloud shell.

  * Make sure you select your subscription by:

    ```
    az account set --subscription <name or id>
    ```

    and you have the privileges to create service principals.

  * Execute the following command for creating the authentication file:
  
    ```
    az ad sp create-for-rbac --sdk-auth > my.azureauth
    ```
  
  * Set the AZURE_AUTH_LOCATION environment variable in your Operating System with the path of your authentication file.

* Run the code:

  ```
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
  