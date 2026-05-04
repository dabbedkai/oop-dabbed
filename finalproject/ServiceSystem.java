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
            System.out.println("\n===========================================");
            System.out.println("        MAIN SERVICE CENTER MENU");
            System.out.println("===========================================");
            System.out.println("1. Grocery Store Services");
            System.out.println("2. Movie Services");
            System.out.println("3. Music Album Services");
            System.out.println("4. Back to Main System");
            System.out.print("Enter Choice: ");

            String choice = MainSystem.scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    serviceMenu("GROCERY", GROCERY_FILE, 3);
                    break;
                case "2":
                    serviceMenu("MOVIE", MOVIE_FILE, 7);
                    break;
                case "3":
                    serviceMenu("MUSIC", MUSIC_FILE, 5);
                    break;
                case "4":
                    System.out.println("returning to main system...");
                    running = false;
                    break;
                default:
                    System.out.println("invalid option.");
                    break;
            }
        }
    }

    private static void serviceMenu(String serviceName, String fileName, int recordSize) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- " + serviceName + " MENU ---");
            System.out.println("1. Add Item");
            System.out.println("2. Search Item");
            System.out.println("3. Remove Item");
            System.out.println("4. Display All Items");
            System.out.println("5. Sort Items");
            System.out.println("6. Back");
            System.out.print("Select: ");

            String choice = MainSystem.scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAdd(serviceName, fileName);
                    break;
                case "2":
                    handleSearch(fileName, recordSize);
                    break;
                case "3":
                    handleRemove(fileName, recordSize);
                    break;
                case "4":
                    displayAllRecords(fileName, recordSize);
                    break;
                case "5":
                    sortRecords(fileName, recordSize);
                    break;
                case "6":
                    inMenu = false;
                    break;
                default:
                    System.out.println("invalid choice.");
                    break;
            }
        }
    }

    private static void handleAdd(String service, String fileName) {
        ArrayList<String> data = new ArrayList<>();
        System.out.println("Adding new " + service + " entry...");

        if (service.equals("GROCERY")) {
            System.out.print("Product Name: "); data.add(MainSystem.scanner.nextLine());
            System.out.print("Price: ");        data.add(MainSystem.scanner.nextLine());
            System.out.print("Quantity: ");     data.add(MainSystem.scanner.nextLine());
        } else if (service.equals("MOVIE")) {
            System.out.print("Type (DVD/VCD): ");  data.add(MainSystem.scanner.nextLine());
            System.out.print("Movie Title: ");     data.add(MainSystem.scanner.nextLine());
            System.out.print("Category: ");        data.add(MainSystem.scanner.nextLine());
            System.out.print("Minutes: ");         data.add(MainSystem.scanner.nextLine());
            System.out.print("Setting: ");         data.add(MainSystem.scanner.nextLine());
            System.out.print("Rental/Sales: ");    data.add(MainSystem.scanner.nextLine());
            System.out.print("Price: ");           data.add(MainSystem.scanner.nextLine());
        } else if (service.equals("MUSIC")) {
            System.out.print("Album Name: ");   data.add(MainSystem.scanner.nextLine());
            System.out.print("Artist: ");       data.add(MainSystem.scanner.nextLine());
            System.out.print("Genre: ");        data.add(MainSystem.scanner.nextLine());
            System.out.print("Record Label: "); data.add(MainSystem.scanner.nextLine());
            System.out.print("Year: ");         data.add(MainSystem.scanner.nextLine());
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            writeFormattedRecord(service, bw, data);
            bw.close();
            System.out.println("item added successfully!");
        } catch (IOException e) {
            System.out.println("error saving item.");
        }
    }

    private static void handleSearch(String fileName, int blockSize) {
        String service = getServiceName(fileName);
        System.out.print("Enter keyword to search: ");
        String keyword = MainSystem.scanner.nextLine().toLowerCase();

        List<List<String>> records = readAllRecords(fileName, blockSize);
        boolean found = false;
        int count = 0;

        // basic loop instead of stream
        for (int i = 0; i < records.size(); i++) {
            List<String> record = records.get(i);
            boolean match = false;
            for (String field : record) {
                if (field.toLowerCase().contains(keyword)) {
                    match = true;
                    break;
                }
            }
            if (match) {
                count++;
                printReceipt(record, service, count);
                found = true;
            }
        }

        if (!found) {
            System.out.println("no records found.");
        }
    }

    private static void handleRemove(String fileName, int blockSize) {
        String service = getServiceName(fileName);
        System.out.print("Enter exact first field (Name/Title) to remove: ");
        String term = MainSystem.scanner.nextLine().trim().toLowerCase();

        List<List<String>> records = readAllRecords(fileName, blockSize);
        List<List<String>> remaining = new ArrayList<>();
        boolean deleted = false;

        for (List<String> record : records) {
            String firstField = record.isEmpty() ? "" : record.get(0).toLowerCase();
            if (!deleted && firstField.equalsIgnoreCase(term)) {
                System.out.println("record deleted: " + record.get(0));
                deleted = true;
            } else {
                remaining.add(record);
            }
        }

        if (deleted) {
            writeAllRecords(service, fileName, remaining);
        } else {
            System.out.println("item not found.");
        }
    }

    private static void displayAllRecords(String fileName, int blockSize) {
        String service = getServiceName(fileName);
        List<List<String>> records = readAllRecords(fileName, blockSize);
        
        if (records.isEmpty()) {
            System.out.println("no records found.");
            return;
        }

        int count = 0;
        for (List<String> record : records) {
            count++;
            printReceipt(record, service, count);
        }
    }

    private static void sortRecords(String fileName, int blockSize) {
        String service = getServiceName(fileName);
        List<List<String>> records = readAllRecords(fileName, blockSize);

        if (records.isEmpty()) {
            System.out.println("no records to sort.");
            return;
        }

        // basic bubble sort by the first field (alphabetical)
        for (int i = 0; i < records.size() - 1; i++) {
            for (int j = 0; j < records.size() - i - 1; j++) {
                String val1 = records.get(j).isEmpty() ? "" : records.get(j).get(0);
                String val2 = records.get(j + 1).isEmpty() ? "" : records.get(j + 1).get(0);
                
                if (val1.compareToIgnoreCase(val2) > 0) {
                    List<String> temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }

        writeAllRecords(service, fileName, records);
        System.out.println("items sorted alphabetically.");
    }

    // --- HELPER METHODS ---

    private static String getServiceName(String fileName) {
        if (GROCERY_FILE.equalsIgnoreCase(fileName)) return "GROCERY";
        if (MOVIE_FILE.equalsIgnoreCase(fileName)) return "MOVIE";
        if (MUSIC_FILE.equalsIgnoreCase(fileName)) return "MUSIC";
        return "";
    }

    private static List<List<String>> readAllRecords(String fileName, int blockSize) {
        List<List<String>> records = new ArrayList<>();
        try {
            File f = new File(fileName);
            if (!f.exists()) return records;

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            List<String> current = new ArrayList<>();
            boolean inBlock = false;

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                if (trimmed.startsWith("===")) {
                    inBlock = true;
                    continue;
                }
                if (trimmed.startsWith("---")) {
                    if (!current.isEmpty()) {
                        records.add(new ArrayList<>(current));
                        current.clear();
                    }
                    inBlock = false;
                    continue;
                }

                if (inBlock) {
                    // extract value after the colon
                    int sep = trimmed.indexOf(":");
                    if (sep >= 0) {
                        current.add(trimmed.substring(sep + 1).trim());
                    } else {
                        current.add(trimmed);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("error reading file.");
        }
        return records;
    }

    private static void writeAllRecords(String service, String fileName, List<List<String>> records) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for (List<String> record : records) {
                writeFormattedRecord(service, bw, record);
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("error writing to file.");
        }
    }

    private static void writeFormattedRecord(String service, BufferedWriter bw, List<String> values) throws IOException {
        bw.write("========================================");
        bw.newLine();
        for (int i = 0; i < values.size(); i++) {
            String label = getFieldLabel(service, i);
            bw.write(label + values.get(i));
            bw.newLine();
        }
        bw.write("----------------------------------------");
        bw.newLine();
        bw.newLine();
    }

    private static String getFieldLabel(String service, int index) {
        if (service.equals("GROCERY")) {
            if (index == 0) return "Product Name: ";
            if (index == 1) return "Price: ";
            if (index == 2) return "Quantity: ";
        } else if (service.equals("MOVIE")) {
            if (index == 0) return "Type: ";
            if (index == 1) return "Movie Title: ";
            if (index == 2) return "Category: ";
            if (index == 3) return "Minutes: ";
            if (index == 4) return "Setting: ";
            if (index == 5) return "Rental/Sales: ";
            if (index == 6) return "Price: ";
        } else if (service.equals("MUSIC")) {
            if (index == 0) return "Album Name: ";
            if (index == 1) return "Artist: ";
            if (index == 2) return "Genre: ";
            if (index == 3) return "Record Label: ";
            if (index == 4) return "Year: ";
        }
        return "";
    }

    private static void printReceipt(List<String> fields, String service, int receiptNumber) {
        System.out.println("\n========================================");
        System.out.println("               RECEIPT #" + receiptNumber);
        System.out.println("========================================");
        for (int i = 0; i < fields.size(); i++) {
            System.out.println(getFieldLabel(service, i) + fields.get(i));
        }
        System.out.println("----------------------------------------");
    }
}
