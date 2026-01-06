package pt.pes.explorer;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * Representa um painel de ficheiros no explorador.
 * Cada painel mostra o conteúdo de um diretório
 * e mantém o estado da seleção atual.
 */
public class FilePanel {
    private final int posicaox;
    private final int larguraPainel;
    private Path diretorioAtual;
    private List<Path> ficheiros;
    private int indiceSelecionado = 0;
    private final FileManager gestorFicheiros;


        /**
     * Cria um novo painel de ficheiros.
     *
     * @param posicaoX         posição horizontal inicial
     * @param larguraPainel   largura do painel
     * @param diretorioInicial diretório inicial a mostrar
     * @param gestorFicheiros classe responsável por operações de ficheiros
     */
    public FilePanel(int posicaox , int largura, Path diretorioInicial, FileManager gestorFicheiros) throws IOException{
        this.posicaox = posicaox;
        this.larguraPainel = largura;
        this.diretorioAtual = diretorioInicial;
        this.gestorFicheiros = gestorFicheiros;
        atualizarConteudo();
    }

    /**
     * Atualiza a lista de ficheiros do diretório atual.
     * Mantém o índice selecionado dentro dos limites.
     */
    public void atualizarConteudo() throws IOException{
        ficheiros = gestorFicheiros.listarDiretorio(diretorioAtual);
        indiceSelecionado = Math.min(indiceSelecionado, ficheiros.size()-1);
        if( indiceSelecionado < 0) indiceSelecionado = 0;
    }

    /**
     * Desenha o painel no ecrã.
     *
     * @param screen ecrã Lanterna
     */
    public void desenhar(Screen screen){
        TextGraphics tg = screen.newTextGraphics();

        for(int i = 0; i < ficheiros.size(); i++){
            String nome = ficheiros.get(i).getFileName().toString();

            if( i == indiceSelecionado){
                tg.setBackgroundColor(TextColor.ANSI.WHITE);
                tg.setForegroundColor(TextColor.ANSI.BLACK);
            }else{
                tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
                tg.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
            tg.putString(posicaox,i+1, ajustarTexto(nome));
        }
    }

    /**
     * Ajusta o texto para caber na largura do painel.
     */
    private String ajustarTexto(String texto){
        if(texto.length() > larguraPainel- 1){
            return texto.substring(0, larguraPainel-1);
        }
        return String.format("%-" + (larguraPainel-1) + "s", texto);
    }
    /**
     * Mover para Cima.
     */
    public void moverParaCima(){
        if(indiceSelecionado > 0) indiceSelecionado--;
    }

    /**
     * Mover para Baixo.
     */
    public void moverParaBaixo(){
        if(indiceSelecionado < ficheiros.size() -1) indiceSelecionado++;
    }
    
    /**
     * Obter o ficheiro selecionado.
     * @return Ficheiro selecionado.
     */
    public Path getFicheiroSelecionado(){
        return ficheiros.isEmpty() ? null : ficheiros.get(indiceSelecionado);
    }

    /**
     * Obter o diretorio atual.
     * @return Diretorio atual.
     * @throws IOException
     */
    public Path getDiretorioAtual() throws IOException{
        return diretorioAtual;
    }

    /**
     * Entra no diretório atualmente selecionado.
     */
    public void entrarDiretorioSelecionado() throws IOException{
        Path selecionado = getFicheiroSelecionado();

        if(selecionado != null && Files.isDirectory(selecionado)) {
            diretorioAtual = selecionado;
            atualizarConteudo();
        }
    }

    /**
     * Volta ao diretório pai.
     */
    public void voltarDiretorio() throws IOException{
        Path pai = diretorioAtual.getParent();

        if(pai != null){
            diretorioAtual = pai;
            atualizarConteudo();
        }
    }

    public void apagarFicheiroSelecionado(Screen ecra) throws IOException{
        Path selecionado = getFicheiroSelecionado();
        if(selecionado == null) return;

        TextGraphics tg = ecra.newTextGraphics();
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setBackgroundColor(TextColor.ANSI.RED);
        tg.putString(2,ecra.getTerminalSize().getRows()-2, "Apagar "+ selecionado.getFileName() + "? (s/n)");
        ecra.refresh();

        KeyStroke tecla = ecra.readInput();
        if(tecla != null && tecla.getCharacter() != null && tecla.getCharacter() == 's'){
            gestorFicheiros.delete(selecionado);
            atualizarConteudo();
        }
    }

    /**
     * Lê texto digitado pelo utilizador no fundo do ecã.
     * 
     * @param ecra
     * @param mensagem
     * @return
     * @throws IOException
     */
    private String lerTexto(Screen ecra, String mensagem) throws IOException{
        TextGraphics tg = ecra.newTextGraphics();
        int linha = ecra.getTerminalSize().getRows();

        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(2,linha, " ".repeat(ecra.getTerminalSize().getColumns()-4));
        tg.putString(2,linha, mensagem);
        ecra.refresh();

        StringBuilder texto = new StringBuilder();

        while(true){
            KeyStroke tecla = ecra.readInput();

            if(tecla.getKeyType() == com.googlecode.lanterna.input.KeyType.Enter){
                break;
            }

            if(tecla.getKeyType() == com.googlecode.lanterna.input.KeyType.Backspace && texto.length()>0){
                texto.deleteCharAt(texto.length()-1);
            }

            if(tecla.getCharacter() != null && !Character.isISOControl(tecla.getCharacter())){
                texto.append(tecla.getCharacter());
            }
            tg.putString(2 + mensagem.length(), linha, String.format("%-30s", toString()));
            ecra.refresh();
        }

        return texto.toString().trim();
    }

    /**
     * Cria um novo ficheiro no diretorio atual.
     * @param ecra onde sera criado o novo Ficheiro.
     */
    public void criarFicheiro(Screen ecra) throws IOException{
        String nome = lerTexto(ecra, "Nome do ficheiro: ");

        if(!nome.isEmpty()){
            gestorFicheiros.criarFicheiro(diretorioAtual, nome);
            atualizarConteudo();
        }
    }

    /**
     * Cria um novo diretorio no diretorio(pasta) atual.
     * @param ecra ecra onde sera criado o novo Diretorio.
     */
    public void criarDiretorio(Screen ecra)throws IOException{
        String nome = lerTexto(ecra, "Nome do Diretorio");

        if(!nome.isEmpty()){
            gestorFicheiros.criarDiretorio(diretorioAtual, nome);
            atualizarConteudo();
        }
    }
    
    /**
     * Copia o fichiero ou diretorio atualmente selecionado neste painel para o painel de destino.
     * @param painelDestino painelDestino painel a ser armazenado o ficheiro ou diretorio.
     * @throws IOException
     */
    public void copiarPara(FilePanel painelDestino) throws IOException{
        Path selecionado = getFicheiroSelecionado();

        if( selecionado == null) return;

        gestorFicheiros.copiar(selecionado, painelDestino.getDiretorioAtual());
        painelDestino.atualizarConteudo();
    }

    /**
     *Move o ficheiro ou diretorio atualmente selecionado neste painel para o painei de destino. 
     * @param painelDestino painelDestino para onde sera movido o ficheiro ou diretorio.
     * @throws IOException
     */
    public void moverPara(FilePanel painelDestino) throws IOException{
        Path selecionado = getFicheiroSelecionado();
        if(selecionado == null) return;

        gestorFicheiros.mover(selecionado, painelDestino.getDiretorioAtual());
        atualizarConteudo();
        painelDestino.atualizarConteudo();;
    }
}
