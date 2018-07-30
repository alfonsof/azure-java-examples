# Azure Blob Storage List Container Java example

This folder contains a Java application example that list a Blob storage container on Microsoft Azure.

List the blobs in a Blob Storage container for Azure.



## Requirements

You must have a [Microsoft Azure subscription](https://azure.microsoft.com/).

You must have an Azure storage account.

This code was written for Java 1.8 and Azure SDKs for Java.



## Using the code

* Configure your Azure access.

  The Azure SDKs Libraries for Java allow you to use several authentication schemes.

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

* Configure your storage account.

  An Azure storage account provides a unique namespace to store and access your Azure Storage data objects.
  
  There are two types of storage accounts:
  
    * A general-purpose storage account gives you access to Azure Storage services such as Tables, Queues, Files, Blobs and Azure virtual machine disks under a single account.

    * A Blob storage account is a specialized storage account for storing your unstructured data as blobs (objects) in Azure Storage.
      Blob storage accounts are similar to a existing general-purpose storage accounts and share all the great durability, availability,
      scalability, and performance features that you use today including 100% API consistency for block blobs and append blobs.

      For applications requiring only block or append blob storage, it is recommend using Blob storage accounts.

      Blob storage accounts expose the Access Tier attribute which can be specified during account creation and modified later as needed.

      There are two types of access tiers that can be specified based on your data access pattern:
        * A Hot access tier which indicates that the objects in the storage account will be more frequently accessed.
          This allows you to store data at a lower access cost.
        * A Cool access tier which indicates that the objects in the storage account will be less frequently accessed.
          This allows you to store data at a lower data storage cost.
  
  An storage account can content containers and every container can content blobs.
  
  ```
  Storage Account -|- Container_1 -|- Blob_1
                   |               |
                   |               |- Blob_2
                   |            
                   |- Container_2 -|- Blob_1
                                   |
                                   |- Blob_2
                                   |
                                   |- Blob_3
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
      ```
      DefaultEndpointsProtocol=https;AccountName=ACCOUNT_NAME;AccountKey=ACCOUNT_KEY;EndpointSuffix=core.windows.net
      ```
  
  Although Azure Storage supports both HTTP and HTTPS in a connection string, HTTPS is highly recommended.

  We store the connection string in a properties file (`app.properties`). The file content is:
  
  ```
  DefaultEndpointsProtocol=https
  AccountName=<ACCOUNT_NAME>
  AccountKey=<ACCOUNT_KEY>
  EndpointSuffix=core.windows.net
  ```

  You only need to edit the file `app.properties` and change the values of:
  
    * `<ACCOUNT_NAME>` by the account name of your storage account.
    * `<ACCOUNT_KEY>` by the account key of your storage account.
  
  The application creates a connection string for your Azure storage account using the following format:
  
  ```
  DefaultEndpointsProtocol=https;AccountName=ACCOUNT_NAME;AccountKey=ACCOUNT_KEY;EndpointSuffix=core.windows.net
  ```

* Run the code.

  You must edit the file `app.properties` and change the values of:
  
    * `<ACCOUNT_NAME>` by the account name of your storage account.
    * `<ACCOUNT_KEY>` by the account key of your storage account.

  You must provide 1 parameter:

    * `<CONTAINER_NAME>` = Name of the container

  Run application:
  
  ```
  java -jar azureblobstoragelist.jar container-example
  ```

* Test the application.

  You should see the list of blobs in the Blob Storage container in the Azure storage account.