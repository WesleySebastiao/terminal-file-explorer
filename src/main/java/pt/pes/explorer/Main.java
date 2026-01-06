package pt.pes.explorer;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.nio.file.Path;


/**
 * Classe principal da aplicação.
 * Responsável por inicializar o ecrã,
 * criar os painéis e gerir as teclas.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        Screen ecra= new DefaultTerminalFactory().createScreen();
        ecra.startScreen();

        int larguraTotal=ecra.getTerminalSize().getColumns();
        int larguraPainel=larguraTotal / 2;
        FileManager gestorFicheiros = new FileManager();

        FilePanel painelEsquerdo=new FilePanel(0, larguraPainel, Path.of("."),gestorFicheiros);
        FilePanel painelDireito=new FilePanel(larguraPainel, larguraPainel, Path.of(".."),gestorFicheiros);
        FilePanel painelAtivo = painelEsquerdo;

        while (true) {
            ecra.clear();

            painelEsquerdo.desenhar(ecra);
            painelDireito.desenhar(ecra);
            ecra.refresh();

            KeyStroke tecla = ecra.readInput();
            if(tecla == null){
                continue;
            }

            switch (tecla.getKeyType()) {
                case ArrowDown-> painelAtivo.moverParaBaixo();
                case ArrowUp->painelAtivo.moverParaCima();
                case Enter ->{painelAtivo.entrarDiretorioSelecionado();}
                case Tab -> painelAtivo = (painelAtivo == painelEsquerdo) ? painelDireito : painelEsquerdo;
                case Backspace ->{painelAtivo.voltarDiretorio();}
                case Character ->{
                    if (tecla.getCharacter() == 'f'){
                        painelAtivo.criarFicheiro(ecra);
                    }
                    if(tecla.getCharacter() == 'd'){
                        painelAtivo.criarDiretorio(ecra);
                    }
                }
                case F5 ->{FilePanel destino = (painelAtivo == painelEsquerdo) ? painelDireito : painelEsquerdo;
                    painelAtivo.copiarPara(destino);
                }
                case F6 ->{FilePanel destino =(painelAtivo == painelEsquerdo) ? painelDireito : painelEsquerdo;
                    painelAtivo.moverPara(destino);
                }
                case Delete -> painelAtivo.apagarFicheiroSelecionado(ecra);
                case Escape -> { ecra.stopScreen();
                    return;
                }
            }
        }

    }
}
