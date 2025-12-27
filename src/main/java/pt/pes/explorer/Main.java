package pt.pes.explorer;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        
        Screen ecra = new DefaultTerminalFactory().createScreen();
        ecra.startScreen();

        int larguraTotal = ecra.getTerminalSize().getColumns();
        int larguraPainel = larguraTotal/2;

        FilePanel painelEsquerdo = new FilePanel(0,larguraPainel, Path.of("."));
        FilePanel painelDireito = new FilePanel(larguraPainel, larguraPainel, Path.of(".."));
        FilePanel painelAtivo = painelEsquerdo;

        while(true){
            ecra.clear();

            painelEsquerdo.draw(ecra);
            painelDireito.draw(ecra);
            ecra.refresh();

            KeyStroke chave = ecra.readInput();
            if(chave == null){
                continue;
            }

            if(chave.getKeyType() == KeyType.EOF){
                break;
            }
            switch (chave.getKeyType()) {
                case ArrowUp -> painelAtivo.moverParaCima();
                case ArrowDown -> painelAtivo.moverParaBaixo();
                case Tab ->painelAtivo =( painelAtivo ==painelEsquerdo ) ? painelDireito : painelEsquerdo; 
                case Escape -> {
                    ecra.stopScreen();
                    return;
                }
            }
        }
        ecra.stopScreen();;
    }
    
}
