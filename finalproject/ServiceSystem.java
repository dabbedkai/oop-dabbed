package finalproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceSystem {

    private static final String GROCERY_FILE = "grocery.txt";
    private static final String MOVIE_FILE = "movie.txt";
    private static final String MUSIC_FILE = "music.txt";

    public static void startService() {
        boolean running = true;
        while (running) {
            MainSystem.printHeader("SERVICE CENTER MENU");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 1 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Grocery Store Services");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 2 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Movie Services");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 3 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Music Album Services");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 4 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Back to Main System");
            InteractionLogger.println();
            InteractionLogger.print(MainSystem.YELLOW + "  ➤ Enter Choice: " + MainSystem.RESET);

            String choice = InteractionLogger.getInput();

            switch (choice) {
                case "1": serviceMenu("GROCERY", GROCERY_FILE, 3); break;
                case "2": serviceMenu("MOVIE", MOVIE_FILE, 7); break;
                case "3": serviceMenu("MUSIC", MUSIC_FILE, 5); break;
                case "4": running = false; break;
                default: MainSystem.showError("Invalid option."); break;
            }
        }
    }

    private static void serviceMenu(String serviceName, String fileName, int recordSize) {
        boolean inMenu = true;
        while (inMenu) {
            MainSystem.printHeader(serviceName + " MENU");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 1 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Add Item");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 2 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Search Item");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 3 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Remove Item");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 4 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Display All Items");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 5 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Sort Items (A-Z)");
            InteractionLogger.println("  " + MainSystem.CYAN + "[" + MainSystem.BOLD + " 6 " + MainSystem.RESET + MainSystem.CYAN + "]" + MainSystem.RESET + " Back");
            InteractionLogger.println();
            InteractionLogger.print(MainSystem.YELLOW + "  ➤ Select: " + MainSystem.RESET);

            String choice = InteractionLogger.getInput();

            switch (choice) {
                case "1": handleAdd(serviceName, fileName); break;
                case "2": handleSearch(fileName, recordSize); break;
                case "3": handleRemove(fileName, recordSize); break;
                case "4": displayAllRecords(fileName, recordSize); break;
                case "5": sortRecords(fileName, recordSize); break;
                case "6": inMenu = false; break;
                default: MainSystem.showError("Invalid choice."); break;
            }
        }
    }

    private static void handleAdd(String service, String fileName) {
        MainSystem.printHeader("ADD NEW " + service);
        ArrayList<String> data = new ArrayList<>();

        if (service.equals("GROCERY")) {
            InteractionLogger.print("  Product Name : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Price        : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Quantity     : "); data.add(InteractionLogger.getInput());
        } else if (service.equals("MOVIE")) {
            InteractionLogger.print("  Type (DVD/VCD) : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Movie Title    : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Category       : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Minutes        : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Setting        : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Rental/Sales   : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Price          : "); data.add(InteractionLogger.getInput());
        } else if (service.equals("MUSIC")) {
            InteractionLogger.print("  Album Name   : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Artist       : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Genre        : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Record Label : "); data.add(InteractionLogger.getInput());
            InteractionLogger.print("  Year         : "); data.add(InteractionLogger.getInput());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            writeFormattedRecord(service, bw, data);
            MainSystem.showSuccess("Item successfully added to " + service + "!");
        } catch (IOException e) {
            MainSystem.showError("Error saving data to database.");
        }
        MainSystem.pause();
    }

    private static void handleSearch(String fileName, int blockSize) {
        MainSystem.printHeader("SEARCH DATABASE");
        String service = getServiceName(fileName);
        InteractionLogger.print(MainSystem.YELLOW + "  ➤ Enter keyword to search: " + MainSystem.RESET);
        String keyword = InteractionLogger.getInput().toLowerCase();

        List<List<String>> records = readAllRecords(fileName, blockSize);
        boolean found = false;
        int count = 0;

        for (List<String> record : records) {
            boolean match = false;
            for (String field : record) {
                if (field.toLowerCase().contains(keyword)) {
                    match = true; break;
                }
            }
            if (match) {
                count++;
                printReceipt(record, service, count);
                found = true;
            }
        }

        if (!found) MainSystem.showError("No matching records found.");
        MainSystem.pause();
    }

    private static void handleRemove(String fileName, int blockSize) {
        MainSystem.printHeader("REMOVE RECORD");
        String service = getServiceName(fileName);
        InteractionLogger.print(MainSystem.YELLOW + "  ➤ Enter Exact Name/Title to remove: " + MainSystem.RESET);
        String term = InteractionLogger.getInput().toLowerCase();

        List<List<String>> records = readAllRecords(fileName, blockSize);
        List<List<String>> remaining = new ArrayList<>();
        boolean deleted = false;

        for (List<String> record : records) {
            String firstField = record.isEmpty() ? "" : record.get(0).toLowerCase();
            if (!deleted && firstField.equalsIgnoreCase(term)) {
                MainSystem.showSuccess("Successfully removed: " + record.get(0));
                deleted = true;
            } else remaining.add(record);
        }

        if (deleted) writeAllRecords(service, fileName, remaining);
        else MainSystem.showError("Item not found in database.");
        
        MainSystem.pause();
    }

    private static void displayAllRecords(String fileName, int blockSize) {
        MainSystem.printHeader("DATABASE RECORDS");
        String service = getServiceName(fileName);
        List<List<String>> records = readAllRecords(fileName, blockSize);
        
        if (records.isEmpty()) {
            MainSystem.showError("No records found.");
        } else {
            int count = 0;
            for (List<String> record : records) printReceipt(record, service, ++count);
        }
        MainSystem.pause();
    }

    private static void sortRecords(String fileName, int blockSize) {
        MainSystem.printHeader("SORTING DATABASE");
        String service = getServiceName(fileName);
        List<List<String>> records = readAllRecords(fileName, blockSize);

        if (records.isEmpty()) {
            MainSystem.showError("No records available to sort.");
        } else {
            records.sort((r1, r2) -> {
                String val1 = r1.isEmpty() ? "" : r1.get(0);
                String val2 = r2.isEmpty() ? "" : r2.get(0);
                return val1.compareToIgnoreCase(val2);
            });
            writeAllRecords(service, fileName, records);
            MainSystem.showSuccess("Items successfully sorted alphabetically.");
        }
        MainSystem.pause();
    }

    private static void printReceipt(List<String> fields, String service, int receiptNumber) {
        InteractionLogger.println(MainSystem.BLUE + "  ┌──────────────────────────────────────────────┐");
        InteractionLogger.print(String.format("  │ " + MainSystem.YELLOW + MainSystem.BOLD + "             RECORD #%-23d" + MainSystem.BLUE + " │\n", receiptNumber));
        InteractionLogger.println("  ├──────────────────────────────────────────────┤" + MainSystem.RESET);
        for (int i = 0; i < fields.size(); i++) {
            InteractionLogger.print(String.format(MainSystem.BLUE + "  │ " + MainSystem.CYAN + "%-12s" + MainSystem.WHITE + " : %-28s " + MainSystem.BLUE + "│\n" + MainSystem.RESET, getFieldLabel(service, i), fields.get(i)));
        }
        InteractionLogger.println(MainSystem.BLUE + "  └──────────────────────────────────────────────┘\n" + MainSystem.RESET);
    }

    private static String getFieldLabel(String service, int index) {
        if (service.equals("GROCERY")) {
            if (index == 0) return "Product"; if (index == 1) return "Price"; if (index == 2) return "Qty";
        } else if (service.equals("MOVIE")) {
            if (index == 0) return "Type"; if (index == 1) return "Title"; if (index == 2) return "Category";
            if (index == 3) return "Minutes"; if (index == 4) return "Setting"; if (index == 5) return "Rent/Sale";
            if (index == 6) return "Price";
        } else if (service.equals("MUSIC")) {
            if (index == 0) return "Album"; if (index == 1) return "Artist"; if (index == 2) return "Genre";
            if (index == 3) return "Label"; if (index == 4) return "Year";
        }
        return "Field";
    }

    private static String getServiceName(String fileName) {
        if (GROCERY_FILE.equalsIgnoreCase(fileName)) return "GROCERY";
        if (MOVIE_FILE.equalsIgnoreCase(fileName)) return "MOVIE";
        if (MUSIC_FILE.equalsIgnoreCase(fileName)) return "MUSIC";
        return "";
    }

    private static List<List<String>> readAllRecords(String fileName, int blockSize) {
        List<List<String>> records = new ArrayList<>();
        File f = new File(fileName);
        if (!f.exists()) return records;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            List<String> current = new ArrayList<>();
            boolean inBlock = false;

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                if (trimmed.startsWith("===")) { inBlock = true; continue; }
                if (trimmed.startsWith("---")) {
                    if (!current.isEmpty()) { records.add(new ArrayList<>(current)); current.clear(); }
                    inBlock = false; continue;
                }

                if (inBlock) {
                    int sep = trimmed.indexOf(":");
                    current.add(sep >= 0 ? trimmed.substring(sep + 1).trim() : trimmed);
                }
            }
        } catch (IOException e) {}
        return records;
    }

    private static void writeAllRecords(String service, String fileName, List<List<String>> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (List<String> record : records) writeFormattedRecord(service, bw, record);
        } catch (IOException e) {}
    }

    private static void writeFormattedRecord(String service, BufferedWriter bw, List<String> values) throws IOException {
        bw.write("========================================"); bw.newLine();
        for (int i = 0; i < values.size(); i++) {
            bw.write(getFieldLabel(service, i) + ": " + values.get(i)); bw.newLine();
        }
        bw.write("----------------------------------------"); bw.newLine();
    }
}