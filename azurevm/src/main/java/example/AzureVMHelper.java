/**
 * AzureVMHelper class with methods for managing Azure VM instances
 */

package example;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.*;
import com.microsoft.azure.management.network.PublicIPAddress;
import com.microsoft.azure.management.network.Network;
import com.microsoft.azure.management.network.NetworkInterface;
import com.microsoft.azure.management.resources.ResourceGroup;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.rest.LogLevel;
import com.microsoft.azure.PagedList;
import java.io.File;

public final class AzureVMHelper {

    private static final String RESOURCE_GROUP_NAME    = "AzureExamples";             // Name of the resource group
    private static final Region AZURE_REGION           = Region.EUROPE_WEST;          // Azure Region
    private static final String AVAILABILITY_SET_NAME  = "myExampleAvailabilitySet";  // Name of the availability set
    private static final String SUBNET_NAME            = "myExampleSubnet";           // Name of the subnet
    private static final String VIRTUAL_NETWORK_NAME   = "myExampleVN";               // Name of the virtual network
    private static final String ADDRESS_SPACE          = "10.0.0.0/16";               // Address space
    private static final String SUBNET_ADDRESS_SPACE   = "10.0.0.0/24";               // Subnet address space
    private static final String PUBLIC_IP_NAME         = "myExamplePublicIP";         // Name of the Public IP
    private static final String NETWORK_INTERFACE_NAME = "myExampleNIC";              // Name of the network interface
    private static final String VM_NAME                = "myExampleVM";               // Name of the VM
    private static final KnownLinuxVirtualMachineImage IMAGE_TYPE = KnownLinuxVirtualMachineImage.UBUNTU_SERVER_16_04_LTS; // Image type
    private static final VirtualMachineSizeTypes VM_TYPE          = VirtualMachineSizeTypes.BASIC_A4;  // VM size type
    private static final String VM_USER_NAME           = "user111";                   // User name for VM
    private static final String VM_PASSWORD            = "Mypass232>";                // Password for VM


    private AzureVMHelper() {
    }

    /**
     * Initiate Azure connection
     */
    public static Azure initResources(Azure azure) {
        try {
            // Azure Credentials
            final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));
            azure = Azure.configure()
                        .withLogLevel(LogLevel.BASIC)
                        .authenticate(credFile)
                        .withDefaultSubscription();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Creating Resource Group ...");
        createResourceGroup(azure);

        return azure;
    }

    /**
     * Deallocate Azure resources
     */
    public static void deleteResources(Azure azure) {
        System.out.println("Deleting Resource Group ...");
        azure.resourceGroups().deleteByName(RESOURCE_GROUP_NAME);
    }

    /**
     * List Compute Engine instances
     */
    public static void listVMs(Azure azure) {
        // get a list of VMs in the same resource group as an existing VM
        PagedList<VirtualMachine> resourceGroupVMs = azure.virtualMachines()
                                                    .listByResourceGroup(RESOURCE_GROUP_NAME);

        // for each vitual machine in the resource group, log their name and plan
        for (VirtualMachine virtualMachine : azure.virtualMachines().listByResourceGroup(RESOURCE_GROUP_NAME)) {
            System.out.println("VM: " + virtualMachine.computerName() + " ! ID: " + virtualMachine.id() +
                                " ! has plan " + virtualMachine.plan());
        }
    }

    /**
     * Run an Azure VM instance
     * Create an instance and create a boot disk on the fly
     */
    public static VirtualMachine createVM(Azure azure) {
        // Create Availability Set
        AvailabilitySet availabilitySet = createAvailabilitySet(azure);

        // Create Virtual Network
        Network network = createVirtualNetwork(azure);

        // Create Public IP address
        PublicIPAddress publicIPAddress = createPublicIp(azure);

        // Create Network Interface
        NetworkInterface networkInterface = createVirtualInterface(azure, network, publicIPAddress);

        System.out.println("Creating virtual machine ...");
        VirtualMachine virtualMachine = azure.virtualMachines()
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

        return virtualMachine;
    }

    /**
     * List an Azure VM instance
     */
    public static void listVM(VirtualMachine vm) {
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
    }

    /**
     * Start an Azure VM instance
     */
    public static void startVM(VirtualMachine vm) {
        System.out.println("Starting VM ...");
        vm.start();
    }

    /**
     * Stop an Azure VM instance
     */
    public static void stopVM(VirtualMachine vm) {
        System.out.println("Stopping VM ...");
        vm.powerOff();
    }

    /**
     * Restart an Azure VM instance
     */
    public static void restartVM(VirtualMachine vm) {
        System.out.println("Restarting VM ...");
        vm.restart();
    }

    /**
     * Delete/Deallocate an Azure VM instance
     */
    public static void deleteVM(VirtualMachine vm) {
        System.out.println("Deallocating VM ...");
        vm.deallocate();
    }

    /**
     * Create the resource group
     * All resources must be contained in a Resource group
     */
    private static ResourceGroup createResourceGroup(Azure azure) {
        System.out.println("Creating resource group ...");
        ResourceGroup resourceGroup = azure.resourceGroups()
                                        .define(RESOURCE_GROUP_NAME)
                                        .withRegion(AZURE_REGION)
                                        .create();
        return resourceGroup;
    }

    /**
     * Create the availability set
     * Availability sets make it easier to maintain the virtual machines used by the application
     */
    private static AvailabilitySet createAvailabilitySet(Azure azure) {
        System.out.println("Creating availability set ...");
        AvailabilitySet availabilitySet = azure.availabilitySets()
                                            .define(AVAILABILITY_SET_NAME)
                                            .withRegion(AZURE_REGION)
                                            .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                                            .withSku(AvailabilitySetSkuTypes.MANAGED)
                                            .create();
        return availabilitySet;
    }

    /**
     * Create the virtual network
     * A virtual machine must be in a subnet of a Virtual network
     */
    private static Network createVirtualNetwork(Azure azure) {
        System.out.println("Creating virtual network ...");
        Network network = azure.networks()
                                .define(VIRTUAL_NETWORK_NAME)
                                .withRegion(AZURE_REGION)
                                .withExistingResourceGroup(RESOURCE_GROUP_NAME)
                                .withAddressSpace(ADDRESS_SPACE)
                                .withSubnet(SUBNET_NAME,SUBNET_ADDRESS_SPACE)
                                .create();
        return network;
    }

    /**
     * Create the public IP address
     * A Public IP address is needed to communicate with the virtual machine
     */
    private static PublicIPAddress createPublicIp(Azure azure) {
        System.out.println("Creating public IP address ...");
        PublicIPAddress publicIPAddress = azure.publicIPAddresses()
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
    private static NetworkInterface createVirtualInterface(Azure azure, Network network, PublicIPAddress publicIPAddress) {
        System.out.println("Creating network interface ...");
        NetworkInterface networkInterface = azure.networkInterfaces()
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
}
