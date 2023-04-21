import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class TextFileProcessor {

    public static void main(String[] args) {
        File inputFile = new File("games.txt"); // replace with the path to your input file
        File outputFile = new File("first_moves.txt"); // replace with the path to your desired output file
        try {
            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(outputFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split("\\s+");
                int endIndex = Math.min(words.length, 10);
                String[] wordsToPrint = Arrays.copyOfRange(words, 0, endIndex);
                writer.println(String.join(" ", wordsToPrint));
                if (endIndex == words.length) {
                    continue;
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
