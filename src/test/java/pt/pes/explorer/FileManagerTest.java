package pt.pes.explorer;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private static Path diretorioTemporario;
    private FileManager gestorFicheiros;

    @BeforeAll
    static void criarDiretorioTemporario() throws IOException {
        diretorioTemporario = Files.createTempDirectory("testeFileManager");
    }

    @AfterAll
    static void apagarDiretorioTemporario() throws IOException {
        if (Files.exists(diretorioTemporario)) {
            Files.walk(diretorioTemporario)
                    .sorted((a, b) -> b.compareTo(a)) //os ficheiros devem ser eliminados antes de apagar o diretorio.
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    @BeforeEach
    void setUp() {
        gestorFicheiros = new FileManager();
    }

    @Test
    void testCriarFicheiroEListarDiretorio() throws IOException {
        gestorFicheiros.criarFicheiro(diretorioTemporario, "teste.txt");

        Path ficheiro = diretorioTemporario.resolve("teste.txt");
        assertTrue(Files.exists(ficheiro), "O ficheiro deve existir");

        List<Path> lista = gestorFicheiros.listarDiretorio(diretorioTemporario);
        assertTrue(lista.contains(ficheiro), "O ficheiro deve aparecer na listagem");
    }

    @Test
    void testCriarDiretorio() throws IOException {
        gestorFicheiros.criarDiretorio(diretorioTemporario, "novaPasta");

        Path pasta = diretorioTemporario.resolve("novaPasta");
        assertTrue(Files.exists(pasta), "O diretório deve existir");
        assertTrue(Files.isDirectory(pasta), "Deve ser um diretório");
    }

    @Test
    void testApagarFicheiro() throws IOException {
        gestorFicheiros.criarFicheiro(diretorioTemporario, "apagar.txt");

        Path ficheiro = diretorioTemporario.resolve("apagar.txt");
        assertTrue(Files.exists(ficheiro));

        gestorFicheiros.delete(ficheiro);
        assertFalse(Files.exists(ficheiro), "O ficheiro deve ser apagado");
    }

    @Test
    void testCopiarEMoverFicheiro() throws IOException {
        gestorFicheiros.criarFicheiro(diretorioTemporario, "origem.txt");
        gestorFicheiros.criarDiretorio(diretorioTemporario, "destino");

        Path origem = diretorioTemporario.resolve("origem.txt");
        Path destino = diretorioTemporario.resolve("destino");

        gestorFicheiros.copiar(origem, destino);
        assertTrue(Files.exists(destino.resolve("origem.txt")),
                "O ficheiro deve ser copiado");

        gestorFicheiros.mover(origem, destino);
        assertFalse(Files.exists(origem),
                "O ficheiro original deve desaparecer após mover");
        assertTrue(Files.exists(destino.resolve("origem.txt")),
                "O ficheiro deve existir no destino após mover");
    }
}
