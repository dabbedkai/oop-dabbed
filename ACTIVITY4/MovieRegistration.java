package ACTIVITY4;

import java.util.Scanner;

public class MovieRegistration {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
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
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Type: DVD");
                    dvdTotal += 1;
                    break;
                case 2:
                    System.out.println("Type: VCD");
                    vcdTotal += 1;
                    break;
                case 3:
                    System.out.println("Type: Tape");
                    tapeTotal += 1;
            }

            System.out.print("Input title of the Movie: ");
            String movieTitle = sc.nextLine();

            System.out.println("""
                Please select an option:
                1. Horror
                2. Scifi
                3. Drama
                4. Comedy
                5. Cartoons
                """);
            int choice2 = sc.nextInt();
            sc.nextLine();
            switch (choice2) {
                case 1 -> {
                    System.out.println("Category: Horror");
                    Horror += 1;
                }
                case 2 -> {
                    System.out.println("Category: Scifi");
                    Scifi += 1;
                }
                case 3 -> {
                    System.out.println("Category: Drama");
                    Drama += 1;
                }
                case 4 -> {
                    System.out.println("Category: Comedy");
                    Comedy += 1;
                }
                case 5 -> {
                    System.out.println("Category: Cartoons");
                    Cartoons += 1;
                }
            }

            System.out.print("Input minutes: ");
            int minutes = sc.nextInt();
            System.out.println("\nTotal minutes: " + minutes);
            sc.nextLine();

            System.out.print("Input setting: ");
            String setting = sc.nextLine();
            System.out.println("\nSetting for movie: " + setting);

            System.out.println("""
                Please select an option:
                1. Rental
                2. Sales
                """);
            int choice3 = sc.nextInt();
            sc.nextLine();

            switch (choice3) {
                case 1 -> {
                    System.out.println("Type: Rental");
                    rent += 1;
                }
                case 2 -> {
                    System.out.println("Type: Sales");
                    sales += 1;
                }
            }

            System.out.print("Input price: ");
            int price = sc.nextInt();
            System.out.println("\nTotal price: " + price);
            sc.nextLine();

            System.out.print("Register another Movie? (Y/N): ");
            anotherMovie = sc.nextLine();

        } while (anotherMovie.equalsIgnoreCase("y"));

        System.out.printf("""
                Reports:
                For rent : %d
                For sales : %d
                VCD Total : %d
                DVD Total : %d
                Tape Total : %d
                Horror Movies : %d
                Scifi Movies : %d
                Drama Movies : %d
                Comedy Movies : %d
                Cartoons Movies : %d
                """, rent, sales, vcdTotal, dvdTotal, tapeTotal, Horror, Scifi, Drama, Comedy, Cartoons);
    }
}
