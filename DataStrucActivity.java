import java.util.Scanner;

public class DataStrucActivity {

    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int iProd;
        String strProdName, strProdDesc;
        double dQty = 0, dPrice = 0, dDiscount = 0;
        
        System.out.println("Please input the following:");

        System.out.print("Product ID: ");
        iProd = InputInt();

        System.out.print("Name: ");
        strProdName = InputString();

        System.out.print("Description: ");
        strProdDesc = InputString();

        System.out.print("Quantity: ");
        dQty = InputDouble();

        System.out.print("Price: ");
        dPrice = InputDouble();

        System.out.print("Discount: ");
        dDiscount = InputDouble();

        double dSubTotal = ComputeSubTotal(dPrice, dQty, dDiscount);

        DisplayProduct(iProd, strProdName, dPrice, dQty, dDiscount, dSubTotal);
    }

    public static String InputString(){
        String strInput = sc.nextLine();
        return strInput;
    }

    public static int InputInt(){
        int iInput = sc.nextInt();
        sc.nextLine();
        return iInput;
    }

    public static double InputDouble(){
        double dInput = sc.nextDouble();
        sc.nextLine();
        return dInput;
    }

    public static double ComputeSubTotal(double dPrice, double dQty, double dDiscount){
        double dSubTotal = (dPrice * dQty) - dDiscount;
        return dSubTotal;
    }

    public static void DisplayProduct(int id, String name, double price, double qty, double discount, double subtotal){
        System.out.printf("""
                %d %s
                Priced at %.2f for %.2f pieces
                Discounted at %.2f
                Subtotal: %.2f
                """, id, name, price, qty, discount, subtotal);
    }
}
