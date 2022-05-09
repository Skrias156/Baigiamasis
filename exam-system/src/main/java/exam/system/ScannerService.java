package exam.system;

import java.util.Scanner;

public class ScannerService {

    Scanner scanner;

    public ScannerService() {
        scanner = new Scanner(System.in);
    }

    /** Is integer */
    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /** Print text and scan */
    public int scanNumber(String text, int limit) {
        final String ERROR = "Nurodete neteisinga reiksme. Prasome pasirinkti: ";
        System.out.print(text);
        while (true) {
            String value = scanner.next();

            if (isInteger(value)) {
                int number = Integer.parseInt(value);
                if (number > 0 && number <= limit)
                    return number;
            }

            System.out.print(ERROR);
        }
    }

    /** Print text and scan line */
    public String scanLine(String text) {
        System.out.print(text);

        String line = "";
        while (true) {
            line = this.scanner.nextLine();
            if (!line.isEmpty())
                break;
        }
        return line;
    }

    /** Print text and read password */
    public String scanPassword(String text) {
        char[] readPassword = System.console().readPassword(text);
        
        String password = "";
        
        for(int index = 0; index < readPassword.length; index++){
            password += readPassword[index];
        }

        return password;
    }

    /** Print text and scan */
    public String scanText(String text) {
        System.out.print(text);
        return scanner.next();
    }

    /** Close scanner */
    public void close() {
        this.scanner.close();
    }

}
