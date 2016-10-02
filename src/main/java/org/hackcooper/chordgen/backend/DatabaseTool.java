package org.hackcooper.chordgen.backend;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DatabaseTool {

    private static Scanner sc;
    static Stack<UndoRedoRunnable> undoStack = new Stack<>();
    static Stack<UndoRedoRunnable> redoStack = new Stack<>();
    static RatingDatabase rdb;

    static void finalizeAction(UndoRedoRunnable urr) {
        pendingChanges = true;
        redoStack.clear();
        undoStack.push(urr);
    }

    public static void main(String[] args) throws IOException {
        String userHome = System.getProperty("user.home");
        new File(userHome, "Documents/Concertfish").mkdirs();

        sc = new Scanner(System.in);
        String resp = "";
        boolean open = prompt("[O]pen or [C]reate a database? ", s -> s.toLowerCase().startsWith("c") || s.toLowerCase().startsWith("o"), s -> s.toLowerCase().startsWith("o"), "Invalid input, try again: ");
        if (open) {
            openDB();
        } else {
            newDB();
        }
        boolean quit = false;
        while (!quit) {
            char opt = prompt("[E]ntry, [P]rogressions, [U]ndo, [R]edo, [N]ew DB, [O]pen, [S]ave, Save [A]s, [Q]uit? ",
                    s -> true,
                    s -> Character.toLowerCase(s.charAt(0)),
                    "Invalid selection, try again: ");
            switch (opt) {
                case 'e':
                    editEntry();
                    break;
                case 'p':
                    progressionsMenu();
                    break;
                case 'u':
                    if (undoStack.isEmpty()) {
                        System.out.println("Nothing to undo.");
                    } else {
                        UndoRedoRunnable urr = undoStack.pop();
                        urr.undo();
                        redoStack.push(urr);
                    }
                    break;
                case 'r':
                    if (redoStack.isEmpty()) {
                        System.out.println("Nothing to redo.");
                    } else {
                        UndoRedoRunnable urr = redoStack.pop();
                        urr.redo();
                        undoStack.push(urr);
                    }
                case 'n':
                    newDB();
                    break;
                case 'o':
                    openDB();
                    break;
                case 's':
                    saveDB();
                    break;
                case 'a':
                    saveDBas();
                    break;
                case 'q':
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid option.");


            }

        }
        confirmPendingChanges();
        System.out.println("Exiting normally.");
        System.exit(0);
    }

    private static void openDB() throws IOException {
        confirmPendingChanges();
        File f = promptFilename();
        try {
            loadDB(f);
        } catch (IOException e) {
            System.err.println("Fatal database access error occurred; shutting down.");
            e.printStackTrace();
        }
    }

    static boolean pendingChanges = false;

    private static void confirmPendingChanges() throws IOException {
        if (pendingChanges) {
            boolean save = promptBoolean("Do you want to save your changes");
            if (save) {
                saveDB();
            }
        }
    }

    private static void saveDB() throws IOException {
        if (rdb.f == null) {
            saveDBas();
        } else {
            wrapVoidWithIO(rdb::save, "Saving database...");
        }
        pendingChanges = false;
    }

    private static void saveDBas() throws IOException {
        rdb.f = promptFilename();
        saveDB();

    }

    private static boolean promptBoolean(String prompt) {
        return prompt(String.format("%s [Y/N}?", prompt),
                s -> s.toLowerCase().startsWith("y") || s.toLowerCase().startsWith("n"), s -> s.toLowerCase().startsWith("y"), "Invalid input, try again: ");
    }

    private static void loadDB(File f) throws IOException {
        rdb = wrapCallWithIO(() -> new RatingDatabase(f), "Loading database from file... ");

        clearUndoRedo();
    }

    private static void newDB() throws IOException {
        confirmPendingChanges();
        rdb = wrapCall(RatingDatabase::new, "Generating a database... ");

        clearUndoRedo();
    }

    private static void clearUndoRedo() {
        undoStack.clear();
        redoStack.clear();
    }

    private static void editEntry() {
        System.err.println("Not yet implemented. Please poke Andrey with sharp stick.");
    }

    private static void progressionsMenu() {
        System.err.println("Not yet implemented. Please poke Andrey with sharp stick.");
    }

    public static <T> T wrapCallWithIO(SupplierWithIO<T> supp, String message) throws IOException {
        System.out.print(message);
        T t = supp.getWithIO();
        System.out.println("[DONE]");
        return t;
    }

    public static void wrapVoidWithIO(RunnableWithIO r, String message) throws IOException {
        System.out.print(message);
        r.runWithIO();
        System.out.println("[DONE]");

    }

    public static <T> T wrapCall(Supplier<T> supp, String message) {
        System.out.print(message);
        T t = supp.get();
        System.out.println("[DONE]");
        return t;
    }

    public static void wrapVoid(Runnable r, String message) {
        System.out.print(message);
        r.run();
        System.out.println("[DONE]");

    }

    private static File promptFilename() {
        String userHome = System.getProperty("user.home");
        File dbf = prompt("Enter filename: ", s -> new File(userHome, "Documents/Concertfish/" + s + ".cfdb").exists(), s -> new File(userHome, "Documents/Concertfish/" + s + ".cfdb"), "File does not exist, try again: ");
        return dbf;

    }

    public static String prompt(String prompt, String defval) {
        System.out.print(prompt);
        String resp = sc.nextLine();
        resp = resp.trim();
        if (resp.isEmpty()) return defval;
        return resp;
    }

    public static <T> T prompt(String prompt, Predicate<String> pred, Function<String, T> converter, String errorText) {
        String resp = prompt(prompt, null);
        while (resp == null || !pred.test(resp)) {
            resp = prompt(errorText, null);
        }
        return converter.apply(resp);
    }


    ChordTuple parseTuple(String s) throws IllegalArgumentException {
        String[] components = s.split("[ ,/]+");
        Chord[] chords = new Chord[components.length];
        for (int i = 0; i < components.length; i++) {
            chords[i] = parseChord(components[i]);
        }
        return new ChordTuple(chords);
    }

    ChordProgression parseProgression(String s) throws IllegalArgumentException {
        String[] components = s.split(" ");
        Chord[] chords = new Chord[components.length];
        for (int i = 0; i < components.length; i++) {
            chords[i] = parseChord(components[i]);
        }
        return new ChordProgression(chords);
    }

    private static Chord parseChord(String s) throws IllegalArgumentException {
        char chord = Character.toUpperCase(s.charAt(0));
        boolean isSharp = s.charAt(1) == '#';
        String designator = s.substring(isSharp ? 2 : 1);
        Chord.Key k = Chord.Key.fromLetter(chord, isSharp);
        if ("M".equals(designator)) {
            return new Chord(k, Chord.ChordType.Major);
        }
        if ("m".equals(designator)) {
            return new Chord(k, Chord.ChordType.Minor);
        }
        designator = designator.toLowerCase();
        switch (designator) {
            case "":
                // empty string is typically major chord
            case "maj":
            case "major":
                return new Chord(k, Chord.ChordType.Major);
            case "min":
            case "minor":
            case "-":
                return new Chord(k, Chord.ChordType.Minor);
            case "seven":
            case "sev":
            case "seventh":
            case "7":
            case "7th":
                return new Chord(k, Chord.ChordType.Seventh);
            default:
                throw new IllegalArgumentException(String.format("%s is not a valid cbord type", designator));
        }

    }


}
