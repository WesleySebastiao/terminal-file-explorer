Projeto de PES.
Nome: Wesley Sebastião.
numeroAluno: Fc58853.

# Terminal File Explorer
    Gestor de ficheiros de comandos, inspirado no "Midnigth Commander", desenvolcido para o projeto de PES.
O projeto segue organização simples mas funcional, dividindo logica de ficheiros da interface(UI):
    *FileManager(Lógica):
        esta classe é responsavel por todas as operaçoes sobre o sistema de focheiros como:
        -Criar ficheiros e diretórios.
        -Apagar ficheiros e diretórios.
        -copiar e mover ficheiros.
        -Listar conteúdos de diretórios.
        ou seja o que envolve manipulaçao real de ficheiros acontece aqui.
        
    *FilePanel(Interface do Utilizador/UI)
        Esta classse representa cada painel no terminal(esquerdo ou direito) e trata da interaçao com utilizador:
        -Navegagar pelos ficheiros com as cetas
        -Selecionando painéis com o Tab.
        -Chamar metodos do FileManager para mover, copiar ou apagar ficheiros.
        -Atualizar dinamcamente o conteúdo do painel conforme mudanças no diretorio.
    
    *Main
        -Ponto de entrada do Programa
        -Inicializa dois FilePanel com diretórios  diferentes.
        -Configura a UI e inicia o loop de interação com o utilizador.
    
# Funcionalidades
-Duas colunas lado a lado com pastas diferentes.
-Navegaçao com "setas":
     - ⬆ / ⬇ : selecionar ficheiro/pasta.
     - ⬅ / ➡ : mover ficheiro entre painéis.
-Tab : alterna os paineis.
-Delete: apaga ficheiros.
-Backspace: volta para a pasta anterior.
-Esc: termina o programa e sai.
-D : cria um novo diretorio.
-F : cria um novo ficheiro.
-Atualizaçao automatica do conteudo dos painéis.


# Compilar.
Na raiz do projeto, compila o projeto com o Maven:

 "mvn clean package"
 
# Executar e abrir 2 Paineis.
Para a execuçao usamos o JAR:

    "java -jar target/terminal-file-explorer-1.0-SNAPSHOT.jar".


# Testes JUnit5
Para ver se os testes estao perfeitos:

    "mvn test".
    
# Executar com Docker
Para construir a imagem Docker (deve estar na raiz do projeto, onde esta o DockFile):

    "sudo docker build -t terminal-file-explorer"
    
E para EXECUTAR CONTAINER: 
    
    "sudo docker run -it --rm terminal-file-explorer"

