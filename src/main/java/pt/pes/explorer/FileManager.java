package pt.pes.explorer;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {
    public List<Path> listarDiretorio(Path directory) throws IOException {
        try (Stream<Path> stream = Files.list(directory)){ 
            return stream.sorted().collect(Collectors.toList());
        }
    }

    public void delete(Path path) throws IOException {
        Files.deleteIfExists(path);
    }
}
