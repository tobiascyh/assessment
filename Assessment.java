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
        boolean completed = true;

        // Program startup
        while (n != 0) {
            while (visited) {
                try {
                    System.out.println("Enter a Wikipedia URL:");
                    input = scan.nextLine().trim();
                    // Validating user provided url
                    url = new URL(input);
                    if (!url.getHost().toLowerCase().contentEquals(wikiURL)) {
                        System.out.println("This is not a Wikipedia URL");
                        completed = false;
                        break;
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
                    completed = false;
                    break;
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
                    completed = false;
                    break;
                }
                // Validating n range
                if (n < 1 || n > 20) {
                    System.out.println("Out of range");
                    completed = false;
                    break;
                }
            }

            // Scrape all links after "/wiki/" path
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
                completed = false;
                break;
            }
            n--;
        }
        scan.close();
        if (completed) {
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
            }
        }
        // Uncomment to view stored values in HashMap
        //System.out.println("Total count:" + total + " Unique count:" + unique);
        //for (String s : storage.keySet()) {
        //    System.out.println(s + " = " + storage.get(s));
        //}
        System.out.println("\nDone");
    }
}
