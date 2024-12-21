import java.util.Scanner;

public class RealEstateApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("===========================================");
            System.out.println("Welcome to PARALLAX Real Estate Application");
            System.out.println("===========================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an Option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    LoginSystem.login();
                    break;
                case "2":
                    LoginSystem.register();
                    break;
                case "3":
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid Option. Please Try Again.");
            }
        }
    }


   
}
