package src;

import java.io.*;
import java.util.EmptyStackException;

public class Main {
    public static void main(String[] args) throws Exception {
        var br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        var nav = new BrowserNavigation();
        for (String line; (line = br.readLine()) != null;) {
            String[] p = line.trim().split("\\s+", 2);
            if (p.length == 0 || p[0].isEmpty())
                continue;
            switch (p[0].toLowerCase()) {
                case "visit":
                    System.out.println(nav.visitWebsite(p.length > 1 ? p[1] : ""));
                    break;
                case "back":
                    try {
                        System.out.println(nav.goBack());
                    } catch (EmptyStackException e) {
                        System.out.println("No previous page available.");
                    }
                    break;
                case "forward":
                    try {
                        System.out.println(nav.goForward());
                    } catch (EmptyStackException e) {
                        System.out.println("No forward page available.");
                    }
                    break;
                case "history":
                    System.out.println(nav.showHistory());
                    break;
                case "clear":
                    System.out.println(nav.clearHistory());
                    break;
                case "save":
                    System.out.println(nav.closeBrowser());
                    break;
                case "restore":
                    System.out.println(nav.restoreLastSession());
                    break;
                case "quit":
                    return;
                default:
                    System.out.println(
                            "Commands: visit <url> | back | forward | history | clear | save | restore | quit");
            }
        }
    }
}
