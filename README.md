INSTRUÇÕES DE COMPILAÇÃO E EXECUÇÃO — PROJETO BIBLIOTECA (Fase 1)
=================================================================

Stack do projeto
----------------
- Linguagem: Java (sem Maven/Gradle)
- Banco: SQLite (arquivo `biblioteca.db` na raiz do projeto)
- Driver JDBC: `sqlite-jdbc-3.50.3.0.jar`
- Ponto de entrada (main): `com.biblioteca.Main`

Pré-requisitos
--------------
1) Java JDK 17+ instalado (verifique com `java -version` e `javac -version`).
2) Estar dentro da pasta do projeto **ProjetoBiblioteca/**, contendo:
   - `src/com/biblioteca/*.java`
   - `sqlite-jdbc-3.50.3.0.jar`
   - (opcional) `biblioteca.db` — será criado automaticamente na primeira execução, se não existir.

Compilação
----------
### Linux / macOS (Bash / Zsh)
> Dentro de `ProjetoBiblioteca/`:
```
mkdir -p out
javac -cp .:sqlite-jdbc-3.50.3.0.jar src/com/biblioteca/*.java -d out
```

### Windows (PowerShell ou CMD)
> Dentro de `ProjetoBiblioteca\`:
```
mkdir out
javac -cp .;sqlite-jdbc-3.50.3.0.jar src\com\biblioteca\*.java -d out
```

Execução (modo console)
-----------------------
### Linux / macOS
```
java -cp out:sqlite-jdbc-3.50.3.0.jar com.biblioteca.Main
```

### Windows
```
java -cp out;sqlite-jdbc-3.50.3.0.jar com.biblioteca.Main
```

Primeira execução — o que acontece?
-----------------------------------
- O arquivo `biblioteca.db` é criado automaticamente (se não existir).
- O método `DatabaseManager.initializeDatabase()` cria as tabelas e **insere dados de exemplo** (autores, livros, usuários, vínculos livro-autor, empréstimos).
- O **menu em modo texto** aparece no console.

Backup exigido no enunciado
---------------------------
Você pode entregar o backup de **duas formas** (não compacte o arquivo):
1) **Arquivo de banco**: enviar o próprio `biblioteca.db` (após rodar a aplicação).
2) **Dump SQL** (recomendado, se tiver `sqlite3`):
   - Linux/macOS:
     ```
     sqlite3 biblioteca.db ".dump" > db/backup.sql
     ```
   - Windows (PowerShell):
     ```
     sqlite3.exe .\biblioteca.db ".dump" > db\backup.sql
     ```

Restauração (opcional para conferência)
---------------------------------------
1) Apague/renomeie o `biblioteca.db` atual.
2) Crie um novo banco e rode o script:
```
sqlite3 biblioteca.db < db/backup.sql
```

Reset do banco
--------------
- Para “zerar”, **exclua `biblioteca.db`** e execute novamente a aplicação.
  O schema e os dados de exemplo serão recriados automaticamente.

Erros comuns
------------
- **`ClassNotFoundException: org.sqlite.JDBC`** → o `sqlite-jdbc-3.50.3.0.jar` não está no classpath (revise `-cp`: `:` em Linux/macOS, `;` em Windows).
- **Sem permissão de escrita no diretório** → abra o terminal como administrador (Windows) ou use uma pasta com permissão.
- **Nada acontece ao rodar** → confirme o `main` correto: `com.biblioteca.Main`.

Sugestão de organização no repositório
--------------------------------------
```
ProjetoBiblioteca/
  sqlite-jdbc-3.50.3.0.jar
  biblioteca.db                # (ou) db/backup.sql
  src/com/biblioteca/
    *.java
  out/                         # gerado após compilar
  docs/
    Projeto_Fase1.pdf
  README_EXECUCAO.txt          # ESTE ARQUIVO
```
