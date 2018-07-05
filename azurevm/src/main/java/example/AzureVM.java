/**
 * Example of how to handle Azure Virtual Machines
 * Using the AzureVMHelper class
 */

package example;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.VirtualMachine;
import java.io.IOException;
import java.util.Scanner;

public class AzureVM {
    /**
     * Print a menu in the screen with the available options
     */
    private static void printMenu() {
        System.out.println("\nMENU");
        System.out.println("0 = Quit");
        System.out.println("1 = List all Virtual Machines");
        System.out.println("2 = Create a Virtual Machine");
        System.out.println("3 = List Virtual Machine");
        System.out.println("4 = Start Virtual Machine");
        System.out.println("5 = Stop Virtual Machine");
        System.out.println("6 = Restart Virtual Machine");
        System.out.println("7 = Delete/Deallocate Virtual Machine");
        System.out.println("Enter an option?");
    }
    /**
     * Read from keyboard the option selected by user
     */
    private static int getOption(Scanner sc) {
        int option;

        String line = sc.nextLine();
        if (line != null && !line.isEmpty() && line.matches("[0-9]+")) {
            option = Integer.parseInt(line);
        } else {
            option = 100;
        }

        return option;
    }

    public static void main(String[] args) throws IOException {
        Azure azure = null;
        VirtualMachine virtualMachine = null;
        Scanner sc = new Scanner(System.in);
        int option;

        azure = AzureVMHelper.initResources(azure);

        do {
            printMenu();
            option = getOption(sc);

            switch (option) {
                case 0:
                    break;
                case 1:  // List all VMs
                    AzureVMHelper.listVMs(azure);
                    break;
                case 2:  // Create VM
                    virtualMachine = AzureVMHelper.createVM(azure);
                    break;
                case 3:  // List VM
                    AzureVMHelper.listVM(virtualMachine);
                    break;
                case 4:  // Start VM
                    AzureVMHelper.startVM(virtualMachine);
                    break;
                case 5:  // Stop VM
                    AzureVMHelper.stopVM(virtualMachine);
                    break;
                case 6:  // Restart VM
                    AzureVMHelper.restartVM(virtualMachine);
                    break;
                case 7:  // Delete/Deallocate VM
                    AzureVMHelper.deleteVM(virtualMachine);
                    virtualMachine = null;
                    break;
                default:
                    System.out.println("ERROR: Enter a valid option!!");
            }
        } while (option != 0);

        AzureVMHelper.deleteResources(azure);
        sc.close();
    }
}
