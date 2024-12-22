import java.util.Scanner;

public class RealEstateApp {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===========================================");
            System.out.println("Welcome to PARALLAX Real Estate Application");
            System.out.println("===========================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");
            System.out.println("\nChoose an Option: ");
            String option = scanner.next();

            switch (option) {
                case "1":
                    LoginSystem.login();
                    break;
                case "2":
                    LoginSystem.register();
                    break;
                case "3":
                    LoginSystem.forgotPassword();
                    break;
                case "4":
                    System.out.println("\nExiting... Thank you!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid Option. Please Try Again.");
            }
        }
    }
}
