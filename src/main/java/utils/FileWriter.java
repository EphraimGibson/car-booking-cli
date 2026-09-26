package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileWriter {
    private final Path path;

    public FileWriter(Path pPath) {
        path = pPath;
    }

    public void writeLineToFile(String line) throws IOException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, CREATE, APPEND)) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
    }


}