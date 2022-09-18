/*
 * Created by: Yee Hong Chu
 * Created date: September 17, 2022
 * Last updated by: Yee Hong Chu
 * Last updated date: September 18, 2022
 *
 * Note: Due to time limitation, feature modules were not separated into individual methods.
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assessment {
    public static void main(String[] args) {
        // Configurable to accommodate different languages
        String language = "en";
        String wikiURL = language + ".wikipedia.org";

        // Initialization
        String input;
        String line;
        int n = -1;
        int total = 0;
        int unique = 0;
        URL url = null;
        Scanner scan = new Scanner(System.in);
        HashMap<String, Integer> storage = new HashMap<String, Integer>();
        boolean visited = true;
        ArrayList<String> history = new ArrayList<String>();

        // Program startup
        while (n != 0) {
            while (visited) {
                try {
                    // Validating user provided url
                    System.out.println("Enter a Wikipedia URL:");
                    input = scan.nextLine().trim();
                    url = new URL(input);
                    if (!url.getHost().toLowerCase().contentEquals(wikiURL)) {
                        System.out.println("This is not a Wikipedia URL");
                        System.exit(0);
                    }
                    // Check input history
                    visited = false;
                    for (String s : history) {
                        if (s.contentEquals(input)) {
                            System.out.println("You have visited this Wikipedia URL");
                            visited = true;
                            break;
                        }
                    }
                    history.add(input);
                } catch (MalformedURLException e) {
                    System.out.println("URL entered is not valid: " + e.getMessage());
                    System.exit(0);
                }
            }
            visited = true;

            // Prompt n for initial startup only
            if (n == -1) {
                try {
                    System.out.println("Run program for number of times (1 to 20):");
                    n = scan.nextInt();
                    scan.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid integer");
                    System.exit(0);
                }
                // Validating n range
                if (n < 1 || n > 20) {
                    System.out.println("Out of range");
                    System.exit(0);
                }
            }

            // Scrape all links after "/wiki/" path and store them
            try {
                InputStream is = url.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    Pattern p = Pattern.compile("a href=\"/wiki/(.*?)\"");
                    Matcher m = p.matcher(line);
                    String path;
                    while (m.find()) {
                        path = m.group(1);
                        String s = url.getProtocol() + "://" + url.getHost() + "/wiki/" + path;
                        if (storage.get(s) == null) {
                            storage.put(s, 1);
                        } else {
                            storage.put(s, storage.get(s) + 1);
                        }
                    }
                }
                br.close();
                is.close();
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                System.exit(0);
            }
            n--;
        }
        scan.close();

        // Write results to file if the program completed successfully
        try {
            FileWriter csvFile = new FileWriter("results.csv");
            for (String s : storage.keySet()) {
                csvFile.append(s);
                csvFile.append("\n");
                total = total + storage.get(s);
                if (storage.get(s) == 1) {
                    unique++;
                }
            }
            csvFile.append(String.valueOf(total));
            csvFile.append("\n");
            csvFile.append(String.valueOf(unique));
            csvFile.append("\n");
            csvFile.flush();
            csvFile.close();
        } catch (IOException e) {
            System.out.println("Failed to create results file");
            System.exit(0);
        }

        // Uncomment to view stored values in HashMap
        //System.out.println("Total count:" + total + " Unique count:" + unique);
        //for (String s : storage.keySet()) {
        //    System.out.println(s + " = " + storage.get(s));
        //}
        System.out.println("\nDone");
    }
}
