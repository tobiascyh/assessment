import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Assessment {
    public static void main(String[] args) {
        // Configurable to accommodate different languages
        String language = "en";
        String wikiURL = language + ".wikipedia.org";

        // Initialization
        String input = "";
        String line = "";
        int n = 0;
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("Enter a  wikipedia link:");
            input = scan.nextLine();
            // Validating user provided url
            URL url = new URL(input);
            if (!url.getHost().contentEquals(wikiURL)) {
                System.out.println("This is not a Wikipedia link");
                System.exit(0);
            }
        } catch (MalformedURLException e) {
            System.out.println("URL entered is not valid: " + e.getMessage());
            System.exit(0);
        }

        try {
            URL url = new URL(input);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            is.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            System.exit(0);
        }

        try {
            System.out.println("Enter an integer between 1 to 20:");
            n = scan.nextInt();
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
}
