package pt.pes.explorer;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Classe responsável por todas as operações
 * sobre o sistema de ficheiros.
 */
public class FileManager {


    /**
     * Lista o conteúdo de um diretório.
     */
    public List<Path> listarDiretorio(Path directory) throws IOException {
        try (Stream<Path> stream = Files.list(directory)){ 
            return stream.sorted().collect(Collectors.toList());
        }
    }

    /**
     * Verifica se o caminho é um diretório.
     */
    public boolean eDiretorio(Path caminho){
        return Files.isDirectory(caminho);
    }
    /**
     * Verifica se o caminho é um ficheiro normal.
     */
    public boolean eFicheiro(Path caminho){
        return Files.isRegularFile(caminho);
    }

    /**
     * Apaga um ficheiro ou diretório vazio.
     */
    public void delete(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    /**
     * Obtém o diretório pai.
     */
    public Path obterDiretoriopai(Path diretorioAtual){
        return diretorioAtual.getParent();
    }

    /**
     * Criar  um novo Ficheiro dentro do diretorio.
     * @param diretorio diretorio onde criar o ficheiro.
     * @param nome nome do ficheiro a ser criado.
     * @throws IOException
     */
    public void criarFicheiro( Path diretorio, String nome) throws IOException{
        if( nome == null || nome.isBlank()){
            return;
        }

        Path novoFicheiro = diretorio.resolve(nome);
        if(Files.exists(novoFicheiro)){
            return;
        }
        Files.createFile(novoFicheiro);
    }

    /**
     * Criar um novo diretório(pastas) dentro de um diretorio.
     * @param diretorio diretório onde criar o novo diretorio.
     * @param nome nome da diretório(pasta) a ser criada.
     * @throws IOException
     */
    public void criarDiretorio(Path diretorio, String nome) throws IOException{
        if(nome == null || nome.isBlank()){
            return;
        }

        Path novoDiretorio = diretorio.resolve(nome);

        if(Files.exists(novoDiretorio)){
            return;
        }
        Files.createDirectory(novoDiretorio);
    }

    /**
     * Copia o Ficheiro selecionado do painel ativo para o diretorio atual de outro painel.
     * @param origem origem do ficheiro que queremos copiar
     * @param destino destino para onde o ficheiro vai ser colado.
     * @throws IOException
     */
    public void copiar(Path origem, Path destino)throws IOException{
        Files.copy(origem,destino.resolve(origem.getFileName()),StandardCopyOption.REPLACE_EXISTING);
    }
    
    /**
     * Move o ficheiro selecionado para o outro painel removendo do locar original para o outro.
     * @param origem origem do ficheiro a ser movido.
     * @param destino destiinho do pasta onde o ficheiro a ser movido vai se colocado.
     * @throws IOException
     */
    public void mover(Path origem, Path destino) throws IOException{
        Files.move(origem, destino.resolve(origem.getFileName()),StandardCopyOption.REPLACE_EXISTING);
    }
}
