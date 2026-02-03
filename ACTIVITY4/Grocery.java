package ACTIVITY4;
import java.util.ArrayList;
import java.util.Scanner;

public class Grocery {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

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

            do {

                System.out.print("Input Product Name: ");
                strProdName = input.nextLine();
                arrProducts.add(strProdName);

                System.out.print("Input Price: ");
                dPrice = input.nextDouble();
                input.nextLine();
                arrPrices.add(dPrice);

                System.out.print("Input Quantity: ");
                dQty = input.nextDouble();
                input.nextLine();
                arrQuantities.add(dQty);

                dTotal = dPrice * dQty;

                System.out.println("Total: " + dTotal);
                dBill = dBill + dTotal;

                System.out.println("Another product Y/N? ");
                strAnotherP = input.nextLine();
                cAnotherP = strAnotherP.charAt(0);

            } while ((cAnotherP == 'Y') || (cAnotherP == 'y'));

            System.out.println("\n============================================================");
            System.out.printf("%39s\n", "GROCERY STORE RECEIPT");
            System.out.println("============================================================");
            System.out.printf("%-20s | %10s | %10s | %10s%n", "PRODUCT", "QTY", "PRICE", "TOTAL");
            System.out.println("------------------------------------------------------------");

            for (int i = 0; i < arrProducts.size(); i++) {
                System.out.printf("%-20s | %10.0f | %10.2f | %10.2f%n", arrProducts.get(i), arrQuantities.get(i), arrPrices.get(i), (arrPrices.get(i) * arrQuantities.get(i)));
            }
            System.out.println("============================================================");
            System.out.printf("%-20s | %36.2f%n", "BILL", dBill);
            System.out.println("============================================================");
            System.out.println("Payment: ");
            dPay = input.nextDouble();
            input.nextLine();

            if (dPay >= dBill) {
                dChange = dPay - dBill;
                System.out.println("\nChange: " + dChange);
                System.out.println("Thank you for shopping");
            } else {
                System.out.println("Money is not enough!");
            }

            System.out.print("Another customer Y/N? ");
            strAnotherP = input.nextLine();
            cAnotherP = strAnotherP.charAt(0);

        } while ((cAnotherP == 'Y') || (cAnotherP == 'y'));

        System.out.println("Grocery program terminating...");
        input.close();
    }
}
