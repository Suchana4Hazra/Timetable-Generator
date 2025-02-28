import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

    private BufferedWriter writer;

    public CSVWriter(String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename));
    }

    public void write(List<String> lines) throws IOException {
        for (String line : lines) {
            writer.write(line);
            writer.newLine(); // Add a new line after each entry
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
