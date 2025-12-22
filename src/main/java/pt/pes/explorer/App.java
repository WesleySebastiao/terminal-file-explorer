package pt.pes.explorer;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class App{
    public static void main( String[] args ){
        
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = terminalFactory.createScreen();

        screen.startScreen();
        screen.clear();

        screen.newTextGraphics().setForegroundColor(TextColor.ANSI.CYAN).putString(2, 2, "Explorador de Arquivos do terminal");
        screen.newTextGraphics().putString(2, 4, "Pressione qualquer tecla para sair");

        screen.refresh();
        screen.readInput();
        screen.stopScreen();
    }
}
