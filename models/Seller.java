package models;


import java.util.Scanner;

import operations.SellerImp;

public class Seller extends User {

    public Seller(String username, String role) {
        super(username, "seller");
    }
   public Seller()
   {
    
   }

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean isLoggedIn = true;
        while (isLoggedIn) {
            System.out.println("\n===== Seller Menu =====");
        System.out.println("1. Create Property");
        System.out.println("2. Edit Property");
        System.out.println("3. Archive Property");
        System.out.println("4. Sign Contract");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");
        String option = scanner.nextLine();


        switch (option) {
            case "1":
                SellerImp.createProperty(getUsername());
                break;
            case "2":
                 SellerImp.editProperty(getUsername()); // called after property selection
                break;
            case "3":
                SellerImp.archiveProperty(getUsername());
                break;
                case "4":
                SellerImp.handleContracts(getUsername());
                break;
            case "5":
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        }
        
    }

   
}