import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class In {

    private Scanner scanner;

    private static final String CHARSET_NAME = "UTF-8";

    private static final Locale LOCALE = Locale.US;

    private static final Pattern WHITESPACE_PATTERN
        = Pattern.compile("\\p{javaWhitespace}+");

    private static final Pattern EVERYTHING_PATTERN
        = Pattern.compile("\\A");

    public In() {
        scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
        scanner.useLocale(LOCALE);
    }

    public In(java.net.Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + socket);
        }
    }

    public In(File file) {
        try {
            scanner = new Scanner(file, CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + file);
        }
    }

    public String readLine() {
        String line;
        try { line = scanner.nextLine(); }
        catch (Exception e) { line = null;               }
        return line;
    }
    
    public String readAll() {
        if (!scanner.hasNextLine())
            return "";

        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }
}