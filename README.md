
### **INSTRUÇÕES DE COMPILAÇÃO E EXECUÇÃO — PROJETO BIBLIOTECA (Fase 1)**

Este documento detalha os pré-requisitos e os passos necessários para compilar e executar a aplicação de gerenciamento da biblioteca a partir do código-fonte.

#### **1. Pré-Requisitos**

Antes de iniciar, certifique-se de que os seguintes componentes estão instalados e configurados em seu sistema:

  * **Java Development Kit (JDK) - Versão 11 ou superior:**

      * **Como verificar se está instalado?** Abra um terminal ou prompt de comando e digite:
        ```sh
        java -version
        ```
      * Se o comando não for reconhecido ou a versão for inferior à 11, você precisará instalar ou atualizar o JDK.

  * **Driver JDBC para SQLite:**
      O driver já está na pasta lib, mas caso haja necessidade de baixar o jdbc novamente siga esses passos:
      * **O que é?** É uma biblioteca (um arquivo `.jar`) que permite que uma aplicação Java se comunique com um banco de dados SQLite. O código depende disso para todas as operações de banco de dados.
      * **Onde obter?** Você pode baixar a versão mais recente do repositório oficial no GitHub:
          * **Link:** [https://github.com/xerial/sqlite-jdbc/releases](https://github.com/xerial/sqlite-jdbc/releases)
          * **Arquivo a baixar:** Procure o arquivo `.jar` mais recente, por exemplo, `sqlite-jdbc-3.50.3.0.jar`.

#### **2. Estrutura de Arquivos**

Para que os comandos funcionem corretamente, organize seus arquivos da seguinte forma.  Baixe a pasta do projeto (`bibliotecaDB`) e  verifique se as subpastas estão organizadas como abaixo:

```
bibliotecaDB/
|
├── lib/
|   └── sqlite-jdbc-3.50.3.0.jar  <-- Coloque o driver baixado aqui
|
├── src/                          <-- Coloque seus arquivos .java aqui
|   └── com/
|       └── biblioteca/
|           ├── Autor.java
|           ├── AutorDAO.java
|           ├── Categoria.java
|           ├── ... (todos os outros arquivos .java)
|           └── UsuarioDAO.java
|
└── (outros arquivos, como o banco de dados 'biblioteca.db' que será criado aqui)
```

#### **3. Passos para Compilação**

A compilação transformará seus arquivos de código-fonte (`.java`) em bytecode executável (`.class`).

1.  **Abra o terminal** ou prompt de comando.

2.  **Navegue até a pasta raiz do seu projeto** (a pasta `ProjetoBiblioteca`).

    ```sh
    cd caminho/para/bibliotecaDB
    ```

3.  **Execute o comando de compilação.** Este comando diz ao compilador Java (`javac`) para criar os arquivos `.class` em uma nova pasta `bin`, usando o driver SQLite que está na pasta `lib`.

      * **No Windows:**
        ```cmd
        javac -d bin -cp "lib\sqlite-jdbc-3.50.3.0.jar" src\com\biblioteca\*.java
        ```
      * **No Linux ou macOS:**
        ```sh
        javac -d bin -cp "lib/sqlite-jdbc-3.50.3.0.jar" src/com/biblioteca/*.java
        ```

    Após a execução, uma nova pasta `bin` será criada com a mesma estrutura de pacotes e os arquivos `.class` dentro dela.

#### **4. Passos para Execução**

Agora que o projeto está compilado, você pode executá-lo.

1.  **No mesmo terminal**, na pasta raiz do projeto, execute o seguinte comando. Ele instrui a Máquina Virtual Java (`java`) a executar a classe `Main`, garantindo que tanto os seus arquivos compilados (na pasta `bin`) quanto o driver SQLite (na pasta `lib`) sejam encontrados.

      * **No Windows:**
        ```cmd
        java -cp "bin;lib\sqlite-jdbc-3.50.3.0.jar" com.biblioteca.Main
        ```
      * **No Linux ou macOS:**
        ```sh
        java -cp "bin:lib/sqlite-jdbc-3.50.3.0.jar" com.biblioteca.Main
        ```

#### **5. O Que Esperar**

Se tudo ocorreu bem, você verá o seguinte:

1.  Um novo arquivo chamado **`biblioteca.db`** será criado na pasta raiz do projeto (`bibliotecaDB/`), caso ele não exista. 
2.  No seu terminal, a aplicação exibirá a mensagem de inicialização e o menu principal:
    ```
    Banco de dados inicializado com sucesso.
    --- MENU PRINCIPAL ---
    1. Operações CRUD
    2. Processos de Negócio
    3. Relatórios do Sistema
    0. Sair
    Escolha uma opção:
    ```

Sua aplicação está funcionando corretamente.



**EXECUTANDO O PROJETO BIBLIOTECA NO INTELLIJ IDEA**

Este guia assume que você já tem o IntelliJ IDEA instalado e os arquivos do projeto organizados conforme discutimos anteriormente.

#### **1. Pré-Requisitos**

1.  **IntelliJ IDEA Community Edition:** A versão gratuita é suficiente.
2.  **JDK (Java Development Kit):** Garanta que você já tenha um JDK (versão 11 ou superior) instalado no seu computador.
3.  **Estrutura do Projeto:** Seus arquivos devem estar organizados da seguinte forma antes de abrir no IntelliJ:
    ```
    bibliotecaDB/
    ├── lib/
    |   └── sqlite-jdbc-[VERSAO].jar
    └── src/
        └── com/
            └── biblioteca/
                └── (todos os seus arquivos .java)
    ```

#### **2. Passo a Passo no IntelliJ IDEA**

**Passo 1: Abrir o Projeto**

1.  Abra o IntelliJ IDEA.
2.  Na tela de boas-vindas, clique em **"Open"**.
3.  Navegue até a sua pasta raiz do projeto (a pasta `ProjetoBiblioteca`) e selecione-a. Clique em **"OK"**.
4.  O IntelliJ irá carregar o projeto. Pode levar um momento para ele indexar os arquivos.

**Passo 2: Configurar o JDK do Projeto**

O IntelliJ precisa saber qual JDK usar para compilar seu código.

1.  Vá em `File` \> `Project Structure...`.
2.  Na janela que abrir, na seção `Project Settings`, selecione `Project`.
3.  No campo **SDK**, certifique-se de que um JDK (ex: `11`, `17`, etc.) está selecionado. Se estiver vazio ou mostrando `<No SDK>`, clique no menu dropdown, selecione `Add SDK` \> `JDK...`, e navegue até a pasta onde seu JDK está instalado.
4.  Clique em **"OK"** para fechar a janela.

**Passo 3: Adicionar o Driver do SQLite como Dependência (O Passo Mais Importante\!)**

Você precisa direcionar o IntelliJ onde encontrar o arquivo `.jar` do SQLite.

1.  Vá novamente em `File` \> `Project Structure...`.
2.  Na seção `Project Settings`, selecione `Modules`.
3.  No painel do meio, com o seu módulo (`ProjetoBiblioteca`) selecionado, clique na aba **`Dependencies`** à direita.
4.  Clique no ícone **`+`** (Add) e selecione **`JARs or directories...`**.
5.  Navegue até a pasta `lib` dentro do seu projeto, selecione o arquivo `sqlite-jdbc-3.50.3.0.jar` e clique em **"OK"**.
6.  A biblioteca agora deve aparecer na lista de dependências. Clique em **"OK"** para salvar e fechar a janela.

**Passo 4: Criar a Configuração de Execução**

1.  No canto superior direito da janela do IntelliJ, você verá um botão que diz **"Add Configuration ou Current File"**. Clique nele e selecione Edit Configurations.
2.  Na janela que abrir, clique no ícone **`+`** no canto superior esquerdo e selecione **`Application`**.
3.  Preencha os seguintes campos:
      * **Name:** Dê um nome para a sua configuração, como `Executar Biblioteca`.
      * **Main class:** Clique no pequeno ícone de pasta ou no botão `...` à direita do campo. Uma caixa de busca aparecerá. Digite `Main` e selecione `com.biblioteca.Main` na lista.
      * **Working directory:** Este campo define onde a aplicação será executada. Para que o arquivo `biblioteca.db` seja criado na raiz do projeto, garanta que este campo leve diretamente para a pasta ProjetoBAN2-main. Normalmente, o IntelliJ já preenche isso automaticamente.
4.  Clique em **"Apply"** e depois em **"OK"**.

**Passo 5: Executar a Aplicação**

1.  A configuração que você criou (`Executar Biblioteca`) agora deve estar selecionada no canto superior direito.
2.  Clique no **ícone verde de "Play" (▶)** ao lado do nome da configuração.

#### **3. Conclusão**

  * Na parte inferior da tela, a janela de ferramentas **"Run"** será aberta.
  * Você verá a saída do seu programa, começando com "Banco de dados inicializado com sucesso." e o menu principal.
  * No painel do projeto à esquerda, o arquivo **`biblioteca.db`** aparecerá na pasta raiz do seu projeto.
