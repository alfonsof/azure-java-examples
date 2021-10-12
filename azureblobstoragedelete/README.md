# Azure Blob Storage Delete Java example

This folder contains a Java application example that handles Blob storage on Microsoft Azure.

Delete a Blob Storage container in an Azure storage account.

## Requirements

* You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

* You must have an Azure storage account.

* The code was written for:
 
  *  Java 8
  *  Apache Maven 3
  *  Azure SDKs for Java

## Using the code

* Configure your Azure access.

    You must create an Azure AD service principal in order to enable application to connect resources into Azure. The service principal grants your application to manage resources in your Azure subscription.

  The Azure SDKs Libraries for Java allow you to use several authentication schemes.

  The application uses an authentication file for authenticating.

  The credentials are taken from `AZURE_AUTH_LOCATION` environment variable.

  For example:

  ```bash
  AZURE_AUTH_LOCATION = /path/to/my.azureauth
  ```

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

* Configure your storage account.

  An Azure storage account contains all of your Azure Storage data objects: blobs, file shares, queues, tables, and disks. The storage account provides a unique namespace for your Azure Storage data that's accessible from anywhere in the world over HTTP or HTTPS. Data in your storage account is durable and highly available, secure, and massively scalable.
  
  An storage account can content containers and every container can content blobs.

  ```bash
  Storage Account
              ├── Container_1/
              │   ├── Blob_1_1/
              │   └── Blob_1_2/
              │
              └── Container_2/
                  ├── Blob_2_1/
                  ├── Blob_2_2/
                  └── Blob_2_3/
  ```

  Create a storage account:
  
  1. Sign in to the Azure portal.
  2. Select the "Storage accounts" option. On the Storage Accounts window that appears, choose Add.
  3. Enter a name for your storage account.
  4. Specify the deployment model to be used: Resource Manager or Classic. Select Resource Manager deployment model.
  5. Select the type of storage account: General purpose or Blob storage. Select General purpose.
  6. Select the geographic location for your storage account. 
  7. Select the replication option for the storage account: LRS, GRS, RA-GRS, or ZRS. Set Replication to Locally Redundant storage (LRS).
  8. Select the subscription in which you want to create the new storage account.
  9. Specify a new resource group or select an existing resource group. 
  10. Click Create to create the storage account.

* Configure your Azure Storage connection string.

  A connection string includes the authentication information required for your application to access data in an Azure Storage account at runtime.

  Your application needs to access the connection string at runtime to authorize requests made to Azure Storage.

  You can find your storage account's connection strings in the Azure portal:
  
    1. Navigate to "Storage Accounts".
    2. Select your storage account.
    3. You can see your connection strings and get your account name and account key.

    ```bash
    DefaultEndpointsProtocol=https;AccountName=ACCOUNT_NAME;AccountKey=ACCOUNT_KEY;EndpointSuffix=core.windows.net
    ```
  
  Although Azure Storage supports both HTTP and HTTPS in a connection string, HTTPS is highly recommended.

  We store the connection string in a properties file (`app.properties`). The file content is:

  ```bash
  DefaultEndpointsProtocol=https
  AccountName=<ACCOUNT_NAME>
  AccountKey=<ACCOUNT_KEY>
  EndpointSuffix=core.windows.net
  ```

  You only need to edit the file `app.properties` and change the values of:
  
  * `<ACCOUNT_NAME>` by the account name of your storage account.
  * `<ACCOUNT_KEY>` by the account key of your storage account.
  
  The application creates a connection string for your Azure storage account using the following format:

  ```bash
  DefaultEndpointsProtocol=https;AccountName=ACCOUNT_NAME;AccountKey=ACCOUNT_KEY;EndpointSuffix=core.windows.net
  ```

* Run the code.

  You must edit the file `app.properties` and change the values of:
  
  * `<ACCOUNT_NAME>` by the account name of your storage account.
  * `<ACCOUNT_KEY>` by the account key of your storage account.

  You must provide 1 parameter:

  * `<CONTAINER_NAME>` = Name of the container

  Run application:

  ```bash
  java -jar azureblobstoragedelete.jar container-example
  ```

* Test the application.

  You should not see the Blob Storage container deleted in an Azure storage account.
