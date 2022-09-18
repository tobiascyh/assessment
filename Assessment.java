import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
        String input = "";
        String line = "";
        int n = -1;
        URL url = null;
        Scanner scan = new Scanner(System.in);
        HashMap<String, Integer> storage = new HashMap<String, Integer>();

        // Program startup
        while (n != 0) {
            try {
                System.out.println("Enter a Wikipedia URL:");
                input = scan.nextLine().trim();
                // Validating user provided url
                url = new URL(input);
                if (!url.getHost().toLowerCase().contentEquals(wikiURL)) {
                    System.out.println("This is not a Wikipedia URL");
                    break;
                }
            } catch (MalformedURLException e) {
                System.out.println("URL entered is not valid: " + e.getMessage());
                break;
            }

            // Prompt n for initial startup only
            if (n == -1) {
                try {
                    System.out.println("Run program for number of times (1 to 20):");
                    n = scan.nextInt();
                    scan.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid integer");
                    break;
                }
                // Validating n range
                if (n < 1 || n > 20) {
                    System.out.println("Out of range");
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
                    String path = "";
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
                break;
            }
            n--;
        }
        scan.close();
        // Uncomment to view stored values in HashMap
        //for (String s : storage.keySet()) {
        //    System.out.println(s + " = " + storage.get(s));
        //}
        System.out.println("\nDone");
    }
}
