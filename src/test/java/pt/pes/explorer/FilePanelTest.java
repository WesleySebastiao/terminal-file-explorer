package pt.pes.explorer;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class FilePanelTest {

    private static Path diretorioTemporario;
    private FileManager gestorFicheiros;
    private FilePanel painel;

    @BeforeAll
    static void criarDiretorioTemporario() throws IOException {
        diretorioTemporario = Files.createTempDirectory("panelTestDir");
    }

    @AfterAll
    static void apagarDiretorioTemporario() throws IOException {
        if (Files.exists(diretorioTemporario)) {
            Files.walk(diretorioTemporario)
                    .sorted((a, b) -> b.compareTo(a)) // apaga ficheiros antes das pastas
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {}
                    });
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        gestorFicheiros = new FileManager();
        painel = new FilePanel(0, 20, diretorioTemporario, gestorFicheiros);
    }

    @Test
    void testAtualizarConteudoESelecao() throws IOException {
        Path ficheiro = Files.createFile(diretorioTemporario.resolve("arquivo1.txt"));

        painel.atualizarConteudo();

        Path selecionado = painel.getFicheiroSelecionado();
        assertNotNull(selecionado);
        assertEquals(ficheiro.getFileName(), selecionado.getFileName());
    }

    @Test
    void testEntrarEVoltarDiretorio() throws IOException {
        Path subpasta = Files.createDirectory(diretorioTemporario.resolve("subpasta"));

        painel.atualizarConteudo();
        painel.entrarDiretorioSelecionado();

        assertEquals(subpasta, painel.getDiretorioAtual());

        painel.voltarDiretorio();
        assertEquals(diretorioTemporario, painel.getDiretorioAtual());
    }
}
