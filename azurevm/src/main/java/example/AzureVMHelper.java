/**
 * AzureVMHelper class with methods for managing Azure VM instances
 */

package example;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import com.azure.resourcemanager.compute.models.KnownLinuxVirtualMachineImage;
import com.azure.resourcemanager.compute.models.VirtualMachine;
import com.azure.resourcemanager.compute.models.VirtualMachineSizeTypes;
import com.azure.resourcemanager.compute.models.AvailabilitySet;
import com.azure.resourcemanager.compute.models.InstanceViewStatus;
import com.azure.resourcemanager.compute.models.DiskInstanceView;
import com.azure.resourcemanager.network.models.Network;
import com.azure.resourcemanager.network.models.NetworkInterface;
import com.azure.resourcemanager.network.models.PublicIpAddress;


public final class AzureVMHelper {

    private static final String RESOURCE_GROUP_NAME    = "AzureExamples";             // Name of the resource group
    private static final Region AZURE_REGION           = Region.EUROPE_WEST;          // Azure Region
    private static final String AVAILABILITY_SET_NAME  = "myExampleAvailabilitySet";  // Name of the availability set
    private static final String VNET_NAME              = "myExampleVN";               // Name of the virtual network
    private static final String VNET_ADDRESS_SPACE     = "10.0.0.0/16";               // Address space
    private static final String SUBNET_NAME            = "myExampleSubnet";           // Name of the subnet
    private static final String SUBNET_ADDRESS_SPACE   = "10.0.0.0/24";               // Subnet address space
    private static final String PUBLIC_IP_NAME         = "myExamplePublicIP";         // Name of the Public IP
    private static final String NETWORK_INTERFACE_NAME = "myExampleNIC";              // Name of the network interface
    private static final String VM_NAME                = "myExampleVM";               // Name of the VM
    private static final KnownLinuxVirtualMachineImage IMAGE_TYPE = KnownLinuxVirtualMachineImage.UBUNTU_SERVER_16_04_LTS; // Image type
    private static final VirtualMachineSizeTypes VM_TYPE          = VirtualMachineSizeTypes.STANDARD_DS1_V2;  // VM size type
    private static final String VM_USER_NAME           = "user111";                   // User name for VM
    private static final String VM_PASSWORD            = "Mypass232>";                // Password for VM

    private static AzureResourceManager azureResourceManager = null;


    private AzureVMHelper() {
    }

    /**
     * Initiate Azure connection
     */
    public static void initResources() {
        try {
            // Acquire the Azure Subscription Id
            String suscription = System.getenv("AZURE_SUBSCRIPTION_ID");
            if (suscription == null) {
                System.out.println("Error: Enviroment variable \"AZURE_SUBSCRIPTION_ID\" does not exist");
                System.exit(1);
            }

            // Azure Credentials
            // AzureProfile picks up the default subscription ID
            // You must set the subscription ID in the AZURE_SUBSCRIPTION_ID environment variable.
            TokenCredential credential = new DefaultAzureCredentialBuilder().build();
            AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
            azureResourceManager = AzureResourceManager.authenticate(credential,profile)
                                                        .withDefaultSubscription();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        createResourceGroup(azureResourceManager);

        return;
    }

    /**
     * Delete Azure resources
     */
    public static void deleteResources() {
        // Delete the Resource Group
        deleteResourceGroup();
    }

    /**
     * Create the resource group
     * All resources must be contained in a Resource group
     */
    private static ResourceGroup createResourceGroup(AzureResourceManager azureResourceManager) {
        System.out.println("Creating Resource Group ...");
        ResourceGroup resourceGroup = azureResourceManager.resourceGroups()
                                        .define(RESOURCE_GROUP_NAME)
                                        .withRegion(AZURE_REGION)
                                        .create();
        System.out.println("Created Resource Group");
        return resourceGroup;
    }

    /**
     * Delete the resource group
     */
    public static void deleteResourceGroup() {
        System.out.println("Deleting Resource Group ...");
        azureResourceManager.resourceGroups().deleteByName(RESOURCE_GROUP_NAME);
        System.out.println("Deleted Resource Group");
    }

    /**
     * Create the availability set
     * Availability sets make it easier to maintain the virtual machines used by the application
     */
    private static AvailabilitySet createAvailabilitySet(AzureResourceManager azureResourceManager) {
        System.out.println("Creating availability set ...");
        AvailabilitySet availabilitySet = azureResourceManager.availabilitySets()
                                            .define(AVAILABILITY_SET_NAME)
                                            .withRegion(AZURE_REGION)
                                            .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                                            .create();
        return availabilitySet;
    }

    /**
     * Create the virtual network
     * A virtual machine must be in a subnet of a Virtual network
     */
    private static Network createVNet(AzureResourceManager azureResourceManager) {
        System.out.println("Creating virtual network ...");
        Network network = azureResourceManager.networks()
                                .define(VNET_NAME)
                                .withRegion(AZURE_REGION)
                                .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                                .withAddressSpace(VNET_ADDRESS_SPACE)
                                .withSubnet(SUBNET_NAME,SUBNET_ADDRESS_SPACE)
                                .create();
        return network;
    }

    /**
     * Create the public IP address
     * A Public IP address is needed to communicate with the virtual machine
     */
    private static PublicIpAddress createPublicIp(AzureResourceManager azureResourceManager) {
        System.out.println("Creating public IP address ...");
        PublicIpAddress publicIPAddress = azureResourceManager.publicIpAddresses()
                                            .define(PUBLIC_IP_NAME)
                                            .withRegion(AZURE_REGION)
                                            .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                                            .withDynamicIP()
                                            .create();
        return publicIPAddress;
    }

    /**
     * Create the network interface
     * A virtual machine needs a network interface to communicate on the virtual network
     */
    private static NetworkInterface createNetworkInterface(AzureResourceManager azureResourceManager,
                                                           Network network, PublicIpAddress publicIPAddress) {
        System.out.println("Creating network interface ...");
        NetworkInterface networkInterface = azureResourceManager.networkInterfaces()
                                                .define(NETWORK_INTERFACE_NAME)
                                                .withRegion(AZURE_REGION)
                                                .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                                                .withExistingPrimaryNetwork(network)
                                                .withSubnet(SUBNET_NAME)
                                                .withPrimaryPrivateIPAddressDynamic()
                                                .withExistingPrimaryPublicIPAddress(publicIPAddress)
                                                .create();
        return networkInterface;
    }

    /**
     * Create a VM instance
     * Create an instance and create a boot disk on the fly
     */
    public static void createVM(AvailabilitySet availabilitySet, NetworkInterface networkInterface) {
        System.out.println("Creating virtual machine ...");
        VirtualMachine vm = azureResourceManager.virtualMachines()
                .define(VM_NAME)
                .withRegion(AZURE_REGION)
                .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                .withExistingPrimaryNetworkInterface(networkInterface)
                .withPopularLinuxImage(IMAGE_TYPE)
                .withRootUsername(VM_USER_NAME)
                .withRootPassword(VM_PASSWORD)
                .withExistingAvailabilitySet(availabilitySet)
                .withSize(VM_TYPE)
                .create();

        System.out.println("Provisioned virtual machine: " + vm.computerName() + " - id: " + vm.id());
    }

    /**
     * List all Azure VMs
     */
    public static void listVMs() {
        System.out.println("\nList VMs in resource group:");
        // For each vitual machine in the resource group, get their information
        for (VirtualMachine virtualMachine : azureResourceManager.virtualMachines().listByResourceGroup(RESOURCE_GROUP_NAME)) {
            System.out.println("- VM: " + virtualMachine.computerName() + " | ID: " + virtualMachine.id() +
                                " | has plan " + virtualMachine.plan());
        }
    }

    /**
     * Run an Azure VM
     * Create an instance and what it needs
     */
    public static void runVM() {
        // Create Availability Set
        AvailabilitySet availabilitySet = createAvailabilitySet(azureResourceManager);

        // Create Virtual Network
        Network network = createVNet(azureResourceManager);

        // Create Public IP address
        PublicIpAddress publicIPAddress = createPublicIp(azureResourceManager);

        // Create Network Interface
        NetworkInterface networkInterface = createNetworkInterface(azureResourceManager, network, publicIPAddress);

        // Create VM
        createVM(availabilitySet, networkInterface);

        return;
    }

    /**
     * List an Azure VM
     */
    public static void listVM() {
        try {
            VirtualMachine vm = azureResourceManager.virtualMachines()
                            .getByResourceGroup(RESOURCE_GROUP_NAME, VM_NAME);

            System.out.println("hardwareProfile");
            System.out.println("    vmSize: " + vm.size());
            System.out.println("storageProfile");
            System.out.println("  imageReference");
            System.out.println("    publisher: " + vm.storageProfile().imageReference().publisher());
            System.out.println("    offer: " + vm.storageProfile().imageReference().offer());
            System.out.println("    sku: " + vm.storageProfile().imageReference().sku());
            System.out.println("    version: " + vm.storageProfile().imageReference().version());
            System.out.println("  osDisk");
            System.out.println("    osType: " + vm.storageProfile().osDisk().osType());
            System.out.println("    name: " + vm.storageProfile().osDisk().name());
            System.out.println("    createOption: " + vm.storageProfile().osDisk().createOption());
            System.out.println("    caching: " + vm.storageProfile().osDisk().caching());
            System.out.println("osProfile");
            System.out.println("    computerName: " + vm.osProfile().computerName());
            System.out.println("    adminUserName: " + vm.osProfile().adminUsername());
            System.out.println("    disablePasswordAuthentication: " + vm.osProfile().linuxConfiguration().disablePasswordAuthentication());
            System.out.println("    ssh: " + vm.osProfile().linuxConfiguration().ssh());
            System.out.println("networkProfile");
            System.out.println("    networkInterface: " + vm.primaryNetworkInterfaceId());
            System.out.println("vmAgent");
            System.out.println("  vmAgentVersion: " + vm.instanceView().vmAgent().vmAgentVersion());
            System.out.println("    statuses");
            for (InstanceViewStatus status : vm.instanceView().vmAgent().statuses()) {
                System.out.println("    code: " + status.code());
                System.out.println("    displayStatus: " + status.displayStatus());
                System.out.println("    message: " + status.message());
                System.out.println("    time: " + status.time());
            }
            System.out.println("disks");
            for (DiskInstanceView disk : vm.instanceView().disks()) {
                System.out.println("  name: " + disk.name());
                System.out.println("  statuses");
                for(InstanceViewStatus status : disk.statuses()) {
                    System.out.println("    code: " + status.code());
                    System.out.println("    displayStatus: " + status.displayStatus());
                    System.out.println("    time: " + status.time());
                }
            }
            System.out.println("VM general status");
            System.out.println("  provisioningStatus: " + vm.provisioningState());
            System.out.println("  id: " + vm.id());
            System.out.println("  name: " + vm.name());
            System.out.println("  type: " + vm.type());
            System.out.println("VM instance status");
            for (InstanceViewStatus status : vm.instanceView().statuses()) {
                System.out.println("  code: " + status.code());
                System.out.println("  displayStatus: " + status.displayStatus());
            }
        } catch (Exception e) {
            System.out.println("\nError:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Start an Azure VM
     */
    public static void startVM() {
        try {
            VirtualMachine vm = azureResourceManager.virtualMachines()
                    .getByResourceGroup(RESOURCE_GROUP_NAME, VM_NAME);

            System.out.println("Starting VM ...");
            vm.start();
            System.out.println("VM Started");
        } catch (Exception e) {
            System.out.println("\nError:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Stop an Azure VM
     */
    public static void stopVM() {
        try {
            VirtualMachine vm = azureResourceManager.virtualMachines()
                    .getByResourceGroup(RESOURCE_GROUP_NAME, VM_NAME);

            System.out.println("Stopping VM ...");
            vm.powerOff();
            System.out.println("VM Stopped");
        } catch (Exception e) {
            System.out.println("\nError:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Restart an Azure VM
     */
    public static void restartVM() {
        try {
            VirtualMachine vm = azureResourceManager.virtualMachines()
                    .getByResourceGroup(RESOURCE_GROUP_NAME, VM_NAME);

            System.out.println("Restarting VM ...");
            vm.restart();
            System.out.println("VM Restarted");
        } catch (Exception e) {
            System.out.println("\nError:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deallocate & Delete an Azure VM
     */
    public static void deleteVM() {
        try {
            VirtualMachine vm = azureResourceManager.virtualMachines()
                .getByResourceGroup(RESOURCE_GROUP_NAME, VM_NAME);

            System.out.println("Deallocating VM ...");
            vm.deallocate();
            System.out.println("VM Deallocated");

            System.out.println("Deleting VM ...");
            azureResourceManager.virtualMachines().deleteById(vm.id());
            System.out.println("VM Deleted");
        } catch (Exception e) {
            System.out.println("\nError:");
            System.out.println(e.getMessage());
        }
    }
}
