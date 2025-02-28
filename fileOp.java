import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class fileOp {
    public static void main(String[] args) {
        // Path to your text file
        String filePath = "semester3.txt";  // Replace with your file path

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into words based on spaces and punctuation
                String[] words = line.split(" ");  // This regular expression splits by non-word characters

                // Print each word
                for (String word : words) {
                    if (!word.isEmpty()) {  // Ignore empty words (in case of multiple spaces or punctuation)
                        System.out.println(word);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
