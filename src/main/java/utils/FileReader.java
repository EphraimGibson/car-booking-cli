package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public List<String> readFile(String fileName) throws IOException {
        List<String> allLines = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new FileNotFoundException(fileName + " not found on classpath");
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                allLines.add(line);
            }
        }
        return allLines;
    }

    public List<String> readFile(Path path) throws IOException {
        try {
            Path parent = path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Unable to initialize booking file: " + path,
                    e
            );
        }

        List<String> allLines = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                allLines.add(line);
            }
        }
        return allLines;
    }
}