# Java examples on Microsoft Azure

This repo contains Java code examples on Microsoft Azure.

These examples show how to use Java 8, Azure Management Libraries for Java and Azure SDKs for Java in order to manage services on Microsoft Azure.

Azure Management Libraries for Java and Azure SDKs for Java allow Java developers to write software that makes use of Azure services like Virtual Machines and Blob storage.

Azure offers two different kinds of API:

* Azure Management Libraries for Java: It is a higher-level, object-oriented API for managing Azure resources, that is optimized for ease of use, succinctness and consistency.
* Azure SDKs for Java: It enables the programmatic consumption of miscellaneous Azure services (not management).

## Quick start

You must have a [Microsoft Azure](https://azure.microsoft.com/) subscription.

You must have an Azure storage account for the Azure Blob Storage examples.

The code for the samples is contained in individual folders on this repository.

For instructions on running the code, please consult the README in each folder.

This is the list of examples:

**Compute - Azure Virtual Machines:**

* [azurevm](/azurevm) - Azure Virtual Machines: Example of how to handle Azure Virtual Machines. It uses Azure Management Libraries for Java.

**Compute - Azure Functions:**

* [azurefunctionhttprequest](/azurefunctionhttprequest) - Azure Function HTTP Request: Example of how to handle an Azure Function that responds to an HTTP request.
* [azurefunctionblobevent](/azurefunctionblobevent) - Azure Function Blob Storage event: Example of how to handle an Azure Function that responds to a Blob Storage event (trigger) when a blob appears in a blob storage.

**Storage - Azure Blob Storage:**

* [azureblobstoragecreate](/azureblobstoragecreate) - Azure Blob Storage Create: Example of how to handle Azure Blob Storage and create a new Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstoragedelete](/azureblobstoragedelete) - Azure Blob Storage Delete: Example of how to handle Azure Blob Storage and delete a Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstoragelist](/azureblobstoragelist) - Azure Blob Storage List: Example of how to handle Azure Blob Storage and list the blobs in a Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstoragelistall](/azureblobstoragelistall) - Azure Blob Storage List All: Example of how to handle Azure Blob Storage and list the blobs in all Blob Storage containers in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstorageupload](/azureblobstorageupload) - Azure Blob Storage Upload: Example of how to handle Azure Blob Storage and upload a local file to a Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstoragedownload](/azureblobstoragedownload) - Azure Blob Storage Download: Example of how to handle Azure Blob Storage and download a Blob from a Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstoragedeleteobject](/azureblobstoragedeleteobject) - Azure Blob Storage Delete Object: Example of how to handle Azure Blob Storage and delete a Blob in a Blob Storage container in an Azure storage account.
* [azureblobstoragecopy](/azureblobstoragecopy) - Azure Blob Storage Copy: Example of how to handle Azure Blob Storage and copy a Blob from a Blob Storage container to another Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.
* [azureblobstoragemove](/azureblobstoragemove) - Azure Blob Storage Move: Example of how to handle Azure Blob Storage and move a Blob from a Blob Storage container to another Blob Storage container in an Azure storage account. It uses Azure SDKs for Java.

## License

This code is released under the MIT License. See LICENSE file.
