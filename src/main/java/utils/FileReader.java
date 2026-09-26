package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader{

    public List<String> readFile(Path path) throws IOException {
        List<String> allLines = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                allLines.add(line);
            }
        }
        return allLines;
    }
}