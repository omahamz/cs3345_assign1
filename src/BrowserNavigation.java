package src;

import java.io.*;
import java.util.EmptyStackException;
import java.awt.Desktop;
import java.net.URI;

public class BrowserNavigation {
    private String currentPage = null;
    private final BrowserStack<String> back = new BrowserStack<>();
    private final BrowserStack<String> fwd = new BrowserStack<>();
    private final BrowserQueue<String> history = new BrowserQueue<>();

    public String visitWebsite(String url) {
        if (currentPage != null)
            back.push(currentPage);
        currentPage = url;
        history.enqueue(url);
        fwd.clear(); // new branch
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                return "Failed to open URL in browser: " + e.getMessage();
            }
        } else {
            return "Desktop browsing not supported on this platform.";
        }
        return "Now at " + url;
    }

    public String goBack() {
        if (back.isEmpty())
            throw new EmptyStackException();
        if (currentPage != null)
            fwd.push(currentPage);
        currentPage = back.pop();
        return "Now at " + currentPage;
    }

    public String goForward() {
        if (fwd.isEmpty())
            throw new EmptyStackException();
        if (currentPage != null)
            back.push(currentPage);
        currentPage = fwd.pop();
        return "Now at " + currentPage;
    }

    public String showHistory() {
        if (history.isEmpty())
            return "No browsing history available.";
        StringBuilder sb = new StringBuilder();
        for (String s : history)
            sb.append(s).append(System.lineSeparator());
        return sb.toString();
    }

    public String clearHistory() {
        history.clear();
        return "Browsing history cleared.";
    }

    public String closeBrowser() {
        // Simple text serialization; each section delimited.
        try (PrintWriter pw = new PrintWriter(new FileWriter("session_data.txt"))) {
            pw.println("#CURRENT");
            pw.println(currentPage == null ? "" : currentPage);

            pw.println("#BACK");
            // Save from top → bottom using iterator
            for (String s : back)
                pw.println(s);

            pw.println("#FWD");
            for (String s : fwd)
                pw.println(s);

            pw.println("#HISTORY");
            for (String s : history)
                pw.println(s);

        } catch (IOException e) {
            return "Error saving session: " + e.getMessage();
        }
        return "Browser session saved.";
    }

    public String restoreLastSession() {
        File f = new File("session_data.txt");
        if (!f.exists())
            return "No previous session found.";

        back.clear();
        fwd.clear();
        history.clear();
        currentPage = null;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String section = "";
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    section = line;
                    continue;
                }
                if (section.equals("#CURRENT")) {
                    currentPage = line.isEmpty() ? null : line;
                } else if (section.equals("#BACK")) {
                    if (!line.isEmpty())
                        back.push(line);
                } else if (section.equals("#FWD")) {
                    if (!line.isEmpty())
                        fwd.push(line);
                } else if (section.equals("#HISTORY")) {
                    if (!line.isEmpty())
                        history.enqueue(line);
                }
            }
        } catch (IOException e) {
            return "Error restoring session: " + e.getMessage();
        }
        return "Previous session restored.";
    }

    // For tests
    public String getCurrentPage() {
        return currentPage;
    }
}
