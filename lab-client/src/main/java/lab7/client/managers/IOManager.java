package lab7.client.managers;


import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * Класс управляющий вводом
 */
public class IOManager {

    private final Deque<Scanner> scanners = new ArrayDeque<>();
    private final Deque<String> scripts = new ArrayDeque<>();
    private boolean scriptMode = false;


    public IOManager() {
        scanners.push(new Scanner(System.in));
    }

    public String readLine() {
        while (!scanners.isEmpty()) {
            try {
                return scanners.peek().nextLine();
            } catch (NoSuchElementException e) {
                if (scanners.size() == 1) {
                    throw new IllegalStateException("Поток ввода закрыт");
                }

                Scanner scanner = scanners.pop();
                scanner.close();
                scripts.pop();

                if (scanners.size() == 1) {
                    scriptMode = false;
                }
            }
        }
        throw new IllegalStateException("Нет сканнеров");
    }

    public String readPassword() {

        Console console = System.console();

        if (console != null) {
            char[] pass = console.readPassword();

            if (pass == null) {
                throw new IllegalStateException("Password is null");
            }
            return new String(pass);
        }
        String line = readLine();

        if (line.isEmpty()) {
            throw new IllegalStateException("Password is empty or null");
        }

        return line;
    }

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public void printError(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }

    public void setFileInput(String fileName) throws FileNotFoundException {
        scriptMode = true;
        scripts.push(fileName);
        scanners.push(new Scanner(new File(fileName)));
    }

    public Deque<String> getScripts() {
        return scripts;
    }

    public boolean isScriptMode() {
        return scriptMode;
    }

}
