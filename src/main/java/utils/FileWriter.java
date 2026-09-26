package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileWriter {
    private final Path path;

    public Path getPath(){
        return path;
    }
    public FileWriter(Path pPath) {
        path = pPath;
    }

    public void writeLineToFile(String line) throws IOException {
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, CREATE, APPEND)) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
    }

    public void writeListToFile(List<String> listToWrite) throws IOException{
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {

            for (String line : listToWrite) {
                writer.write(line);
                writer.newLine();
            }
        }
    }


}