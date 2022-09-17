import java.util.Scanner;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.InputMismatchException;

public class Assessment {
    public static void main(String[] args) {
        String language = "en";
        String wikiURL = language + ".wikipedia.org";

        String input = "";
        int n = 0;
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("Enter a  wikipedia link:");
            input = scan.nextLine();
            URL url = new URL(input);
            if (url.getHost().contentEquals(wikiURL)) {
                System.out.println("This is a Wikipedia link");
            } else {
                System.out.println("This is not a Wikipedia link");
                System.exit(0);
            }
        } catch (MalformedURLException e) {
            System.out.println("URL entered is not valid: " + e.getMessage());
            System.exit(0);
        }


        try {
            System.out.println("Enter an integer between 1 to 20:");
            n = scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid integer");
            System.exit(0);
        }
        if (n < 1 || n > 20) {
            System.out.println("Out of range");
            System.exit(0);
        }
    }
}
