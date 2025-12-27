package pt.pes.explorer;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FilePanel {
    private final int x;
    private final int largura;
    private Path diretorioAtual;
    private List<Path> ficheiros;
    private int indiceSelecionado = 0;

    private final FileManager fileManager = new FileManager();

    public FilePanel(int x , int largura, Path diretorioInicial) throws IOException{
        this.x = x;
        this.largura = largura;
        this.diretorioAtual = diretorioInicial;
        refresh();
    }

    public void refresh() throws IOException{
        ficheiros = fileManager.listarDiretorio(diretorioAtual);
        indiceSelecionado = Math.min(indiceSelecionado, ficheiros.size()-1);
        if( indiceSelecionado < 0) indiceSelecionado = 0;
    }
    public void draw(Screen screen){
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
            tg.putString(x,i+1, pad(nome));
        }
    }

    private String pad(String texto){
        if(texto.length() > largura- 1){
            return texto.substring(0, largura-1);
        }
        return String.format("%-" + (largura-1) + "s", texto);
    }

    public void moverParaCima(){
        if(indiceSelecionado > 0) indiceSelecionado--;
    }

    public void moverParaBaixo(){
        if(indiceSelecionado < ficheiros.size() -1) indiceSelecionado++;
    }
    public Path getFicheiroSelecionado(){
        return ficheiros.isEmpty() ? null : ficheiros.get(indiceSelecionado);
    }

}
