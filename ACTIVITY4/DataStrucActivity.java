package ACTIVITY4;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataStrucActivity {

    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n============================================================");
            System.out.printf("%42s\n", "WELCOME TO THE SERVICE CENTER");
            System.out.println("============================================================");
            System.out.println("1. Grocery Store");
            System.out.println("2. Movie Registration");
            System.out.println("3. Music Album Registration");
            System.out.println("4. Exit");
            System.out.print("Choose a service (1-4): ");
            choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    groceryService();
                    break;
                case 2:
                    movieRegistrationService();
                    break;
                case 3:
                    musicAlbumService();
                    break;
                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);

        input.close();
    }

    public static void groceryService() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GROCERY STORE SESSION ===\n");

        ArrayList<String> arrProducts = new ArrayList<>();
        ArrayList<Double> arrPrices = new ArrayList<>();
        ArrayList<Double> arrQuantities = new ArrayList<>();

        String strAnotherP, strProdName, strCustomer;
        char cCustomer = ' ', cAnotherP = ' ';

        double dQty, dBill, dPrice;
        double dTotal = 0, dPay, dChange = 0;
        System.out.println("\n============================================================");
        System.out.printf("%42s\n", "WELCOME TO THE GROCERY STORE");
        System.out.println("============================================================");
        do {

            dBill = 0;
            arrProducts.clear();
            arrPrices.clear();
            arrQuantities.clear();
            sb.append("New Customer:\n");

            do {

                System.out.print("Input Product Name: ");
                strProdName = sc.nextLine();
                sb.append("Product Name: " + strProdName + "\n");
                arrProducts.add(strProdName);

                System.out.print("Input Price: ");
                dPrice = sc.nextDouble();
                sc.nextLine();
                sb.append("Price: " + dPrice + "\n");
                arrPrices.add(dPrice);

                System.out.print("Input Quantity: ");
                dQty = sc.nextDouble();
                sc.nextLine();
                sb.append("Quantity: " + dQty + "\n");
                arrQuantities.add(dQty);

                dTotal = dPrice * dQty;

                System.out.println("Total: " + dTotal);
                sb.append("Total: " + dTotal + "\n");
                dBill = dBill + dTotal;

                System.out.println("Another product Y/N? ");
                strAnotherP = sc.nextLine();
                sb.append("Another product: " + strAnotherP + "\n");
                cAnotherP = strAnotherP.charAt(0);

            } while ((cAnotherP == 'Y') || (cAnotherP == 'y'));

            System.out.println("\n============================================================");
            System.out.printf("%39s\n", "GROCERY STORE RECEIPT");
            System.out.println("============================================================");
            System.out.printf("%-20s | %10s | %10s | %10s%n", "PRODUCT", "QTY", "PRICE", "TOTAL");
            System.out.println("------------------------------------------------------------");

            sb.append("RECEIPT:\n");
            sb.append(String.format("%-20s | %10s | %10s | %10s%n", "PRODUCT", "QTY", "PRICE", "TOTAL"));
            sb.append("------------------------------------------------------------\n");

            for (int i = 0; i < arrProducts.size(); i++) {
                System.out.printf("%-20s | %10.0f | %10.2f | %10.2f%n", arrProducts.get(i), arrQuantities.get(i), arrPrices.get(i), (arrPrices.get(i) * arrQuantities.get(i)));
                sb.append(String.format("%-20s | %10.0f | %10.2f | %10.2f%n", arrProducts.get(i), arrQuantities.get(i), arrPrices.get(i), (arrPrices.get(i) * arrQuantities.get(i))));
            }
            System.out.println("============================================================");
            System.out.printf("%-20s | %36.2f%n", "BILL", dBill);
            System.out.println("============================================================");
            sb.append("============================================================" + "\n");
            sb.append(String.format("%-20s | %36.2f%n", "BILL", dBill));
            sb.append("============================================================" + "\n");
            System.out.println("Payment: ");
            dPay = sc.nextDouble();
            sc.nextLine();
            sb.append("Payment: " + dPay + "\n");

            if (dPay >= dBill) {
                dChange = dPay - dBill;
                System.out.println("\nChange: " + dChange);
                System.out.println("Thank you for shopping");
                sb.append("Change: " + dChange + "\nThank you for shopping\n");
            } else {
                System.out.println("Money is not enough!");
                sb.append("Money is not enough!\n");
            }

            System.out.print("Another customer Y/N? ");
            strAnotherP = sc.nextLine();
            sb.append("Another customer: " + strAnotherP + "\n");
            cAnotherP = strAnotherP.charAt(0);

        } while ((cAnotherP == 'Y') || (cAnotherP == 'y'));

        System.out.println("Grocery program terminating...");
        sb.append("Grocery program terminating...\n\n");

        try (FileWriter fw = new FileWriter("result.txt", true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void movieRegistrationService() {

        StringBuilder sb = new StringBuilder();
        try {

            int rent = 0, sales = 0, Comedy = 0, Horror = 0, Scifi = 0, Drama = 0, Cartoons = 0, dvdTotal = 0, vcdTotal = 0, tapeTotal = 0;

            System.out.println("Welcome to the Movie Registration!");

            String anotherMovie = "";

            do {

                System.out.println("""
                Please select an option:
                1. DVD
                2. VCD
                3. Tape
                """);

                sb.append("""
                Please select an option:
                1. DVD
                2. VCD
                3. Tape
                """);
                int choice = sc.nextInt();
                sb.append("Option: " + choice + "\n");
                sc.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.println("Type: DVD");
                        sb.append("\nType: DVD\n");
                        dvdTotal += 1;
                    }
                    case 2 -> {
                        System.out.println("Type: VCD");
                        sb.append("\nType: VCD\n");
                        vcdTotal += 1;
                    }
                    case 3 -> {
                        System.out.println("Type: Tape");
                        sb.append("\nType: Tape\n");
                        tapeTotal += 1;
                    }
                }

                System.out.print("Input title of the Movie: ");
                sb.append("Input title of the Movie: ");
                String movieTitle = sc.nextLine();
                sb.append("Title: " + movieTitle + "\n");

                System.out.println("""

                Please select an option:
                1. Horror
                2. Scifi
                3. Drama
                4. Comedy
                5. Cartoons
                """);

                sb.append("""

                Please select an option:
                1. Horror
                2. Scifi
                3. Drama
                4. Comedy
                5. Cartoons
                """);
                int choice2 = sc.nextInt();
                sb.append("Option: " + choice2 + "\n");
                sc.nextLine();
                switch (choice2) {
                    case 1 -> {
                        System.out.println("Category: Horror");
                        sb.append("\nCategory: Horror\n");
                        Horror += 1;
                    }
                    case 2 -> {
                        System.out.println("Category: Scifi");
                        sb.append("\nCategory: Scifi\n");
                        Scifi += 1;
                    }
                    case 3 -> {
                        System.out.println("Category: Drama");
                        sb.append("\nCategory: Drama\n");
                        Drama += 1;
                    }
                    case 4 -> {
                        System.out.println("Category: Comedy");
                        sb.append("Category: Comedy\n");
                        Comedy += 1;
                    }
                    case 5 -> {
                        System.out.println("Category: Cartoons");
                        sb.append("\nCategory: Cartoons\n");
                        Cartoons += 1;
                    }
                }

                System.out.print("Input minutes: ");
                sb.append("Input minutes: ");
                int minutes = sc.nextInt();
                sb.append("Minutes: " + minutes + "\n");
                sb.append("\nTotal minutes: " + minutes + "\n");
                sc.nextLine();

                System.out.print("Input setting: ");
                sb.append("Input setting: ");
                String setting = sc.nextLine();
                sb.append("Setting: " + setting + "\n");

                System.out.println("""
                Please select an option:
                1. Rental
                2. Sales
                """);
                int choice3 = sc.nextInt();
                sc.nextLine();

                sb.append("""

                Please select an option:
                1. Rental
                2. Sales
                """);

                sb.append("Option: " + choice3 + "\n");

                switch (choice3) {
                    case 1 -> {
                        System.out.println("Type: Rental");
                        sb.append("\nType: Rental\n");
                        rent += 1;
                    }
                    case 2 -> {
                        System.out.println("Type: Sales");
                        sb.append("\nType: Sales\n");
                        sales += 1;
                    }
                }

                System.out.print("Input price: ");
                sb.append("Input price: \n");
                int price = sc.nextInt();

                sb.append("Total price: " + price + "\n");
                sc.nextLine();

                System.out.print("Register another Movie? (Y/N): ");
                sb.append("Register another Movie? (Y/N): \n");
                anotherMovie = sc.nextLine();
                sb.append("Another Movie: " + anotherMovie + "\n");

            } while (anotherMovie.equalsIgnoreCase("y"));

            String result = ("""

                Reports:
                For rent :          %d
                For sales :         %d
                VCD Total :         %d
                DVD Total :         %d
                Tape Total :        %d
                Horror Movies :     %d
                Scifi Movies :      %d
                Drama Movies :      %d
                Comedy Movies :     %d
                Cartoons Movies :   %d
                """);
            String resultFormatted = result.formatted(rent, sales, vcdTotal, dvdTotal, tapeTotal, Horror, Scifi, Drama, Comedy, Cartoons);
            sb.append(resultFormatted);
            System.out.println(resultFormatted);
        }catch(Exception e){
            System.out.println("Hello");
        }
        try (FileWriter fw = new FileWriter("result.txt", true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void musicAlbumService() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MUSIC ALBUM REGISTRATION SESSION ===\n");

        try {

            String albumName[] = new String[50];
            String artistName[] = new String[50];
            String genre[] = new String[50];
            String recordLabel[] = new String[50];
            String releaseDate[] = new String[50];

            String anotherAlbum;

            int albumCount = 0;

            System.out.println("Welcome to the Music Album Registration!\n");
            sb.append("Welcome to the Music Album Registration!\n\n");

            do {

                System.out.println("Please input the following:");
                sb.append("Please input the following:\n");

                albumName[albumCount] = InputString("Enter Album Name: ", sb);
                artistName[albumCount] = InputString("Artist/Band Name: ", sb);
                genre[albumCount] = InputString("Genre: ", sb);
                recordLabel[albumCount] = InputString("Record Label: ", sb);
                releaseDate[albumCount] = InputString("Release Date: ", sb);

                albumCount++;
                System.out.print("Register another Album? (Y/N): ");
                anotherAlbum = sc.nextLine();
                sb.append("Register another Album? (Y/N): " + anotherAlbum + "\n");

            } while (anotherAlbum.equalsIgnoreCase("y"));

            DisplayAlbum(albumName, artistName, genre, recordLabel, releaseDate, albumCount, sb);
        }catch(Exception e){
            System.out.println("Hello");
        }

        try (FileWriter fw = new FileWriter("result.txt", true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String InputString(String prompt, StringBuilder sb) {
        System.out.println(prompt);
        sb.append(prompt + "\n");
        Scanner sc = new Scanner(System.in);
        String strInput = sc.nextLine();
        sb.append(strInput + "\n");
        return strInput;
    }

    public static void DisplayAlbum(String[] albumData, String[] artistName, String[] genre, String[] recordLabel, String[] releaseDate, int albumCount, StringBuilder sb) {

        for (int i = 0; i < albumCount; i++) {
            System.out.println("Album Name: " + albumData[i]);
            System.out.println("Artist/Band Name: " + artistName[i]);
            System.out.println("Genre: " + genre[i]);
            System.out.println("Record Label: " + recordLabel[i]);
            System.out.println("Release Date: " + releaseDate[i]);
            sb.append("Album Name: " + albumData[i] + "\n");
            sb.append("Artist/Band Name: " + artistName[i] + "\n");
            sb.append("Genre: " + genre[i] + "\n");
            sb.append("Record Label: " + recordLabel[i] + "\n");
            sb.append("Release Date: " + releaseDate[i] + "\n");
        }

        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", "Album", "Artist", "Genre", "Record", "Released");
        System.out.println("-------------------------------------------------------------");
        sb.append(String.format("%-15s %-15s %-15s %-15s %-15s%n", "Album", "Artist", "Genre", "Record", "Released"));
        sb.append("-------------------------------------------------------------\n");

        for (int i = 0; i < albumCount; i++) {
            System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", albumData[i], artistName[i], genre[i], recordLabel[i], releaseDate[i]);
            sb.append(String.format("%-15s %-15s %-15s %-15s %-15s%n", albumData[i], artistName[i], genre[i], recordLabel[i], releaseDate[i]));
        }
        sb.append("\n");
    }
}
