
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataStrucActivity {

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(System.in)) {

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
                """);
            String resultFormatted = result.formatted(rent, sales, vcdTotal, dvdTotal, tapeTotal, Horror, Scifi, Drama, Comedy, Cartoons);
            sb.append(resultFormatted);
        }
        try (FileWriter fw = new FileWriter("data.txt")) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
