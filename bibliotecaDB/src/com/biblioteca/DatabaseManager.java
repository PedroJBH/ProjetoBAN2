package com.biblioteca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:biblioteca.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS emprestimo;");
            stmt.execute("DROP TABLE IF EXISTS exemplar;"); 
            stmt.execute("DROP TABLE IF EXISTS livro_autor;");
            stmt.execute("DROP TABLE IF EXISTS livro;");
            stmt.execute("DROP TABLE IF EXISTS autor;");
            stmt.execute("DROP TABLE IF EXISTS usuario;");
            stmt.execute("DROP TABLE IF EXISTS editora;");
            stmt.execute("DROP TABLE IF EXISTS categoria;");
            stmt.execute("DROP TABLE IF EXISTS reserva;"); 

            stmt.execute("CREATE TABLE autor (" +
                         "id_autor INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nome TEXT NOT NULL)");

            stmt.execute("CREATE TABLE editora (" +
                         "id_editora INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nome TEXT NOT NULL)");
            
            stmt.execute("CREATE TABLE categoria (" +
                         "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nome TEXT NOT NULL)");

            stmt.execute("CREATE TABLE livro (" +
                         "id_livro INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "titulo TEXT NOT NULL," +
                         "ano_publicacao INTEGER," +
                         "id_editora_fk INTEGER," +
                         "id_categoria_fk INTEGER," +
                         "FOREIGN KEY(id_editora_fk) REFERENCES editora(id_editora) ON DELETE SET NULL," +
                         "FOREIGN KEY(id_categoria_fk) REFERENCES categoria(id_categoria) ON DELETE SET NULL)");

            stmt.execute("CREATE TABLE livro_autor (" +
                         "id_livro_fk INTEGER," +
                         "id_autor_fk INTEGER," +
                         "PRIMARY KEY (id_livro_fk, id_autor_fk)," +
                         "FOREIGN KEY(id_livro_fk) REFERENCES livro(id_livro) ON DELETE CASCADE," +
                         "FOREIGN KEY(id_autor_fk) REFERENCES autor(id_autor) ON DELETE CASCADE)");
            
            stmt.execute("CREATE TABLE usuario (" +
                         "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nome TEXT NOT NULL," +
                         "email TEXT NOT NULL UNIQUE," +
                         "endereco TEXT)");

            stmt.execute("CREATE TABLE exemplar (" +
                         "id_exemplar INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "id_livro_fk INTEGER NOT NULL," +
                         "status TEXT NOT NULL CHECK(status IN ('Disponível', 'Emprestado', 'Reservado'))," +
                         "FOREIGN KEY(id_livro_fk) REFERENCES livro(id_livro) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE emprestimo (" +
                         "id_emprestimo INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "id_exemplar_fk INTEGER NOT NULL," +
                         "id_usuario_fk INTEGER NOT NULL," +
                         "data_emprestimo TEXT NOT NULL," +
                         "data_devolucao TEXT," +
                         "data_prevista_devolucao TEXT NOT NULL," +
                         "FOREIGN KEY(id_exemplar_fk) REFERENCES exemplar(id_exemplar) ON DELETE RESTRICT," +
                         "FOREIGN KEY(id_usuario_fk) REFERENCES usuario(id_usuario) ON DELETE RESTRICT)");

            stmt.execute("CREATE TABLE reserva (" +
                         "id_reserva INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "id_livro_fk INTEGER NOT NULL," +
                         "id_usuario_fk INTEGER NOT NULL," +
                         "data_reserva TEXT NOT NULL," +
                         "FOREIGN KEY(id_livro_fk) REFERENCES livro(id_livro) ON DELETE CASCADE," +
                         "FOREIGN KEY(id_usuario_fk) REFERENCES usuario(id_usuario) ON DELETE CASCADE)");

            insertInitialData(stmt);
            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
    
    private static void insertInitialData(Statement stmt) throws SQLException {

        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('Stephen King');");        
        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('J.R.R. Tolkien');");       
        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('Frank Herbert');");     
        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('Charles Duhigg');");      

        stmt.executeUpdate("INSERT INTO editora (nome) VALUES ('Viking Press');"); 
        stmt.executeUpdate("INSERT INTO editora (nome) VALUES ('Allen & Unwin');");     
        stmt.executeUpdate("INSERT INTO editora (nome) VALUES ('Aleph');");             
        stmt.executeUpdate("INSERT INTO editora (nome) VALUES ('Objetiva');");          
        
        stmt.executeUpdate("INSERT INTO categoria (nome) VALUES ('Terror');");           
        stmt.executeUpdate("INSERT INTO categoria (nome) VALUES ('Fantasia');");           
        stmt.executeUpdate("INSERT INTO categoria (nome) VALUES ('Ficção Científica');");  
        stmt.executeUpdate("INSERT INTO categoria (nome) VALUES ('Auto-ajuda');");         

        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, id_editora_fk, id_categoria_fk) VALUES ('IT: A coisa', 1986, 1, 1);");      
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, id_editora_fk, id_categoria_fk) VALUES ('O Senhor dos Anéis', 1954, 2, 2);"); 
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, id_editora_fk, id_categoria_fk) VALUES ('Duna', 1965, 3, 3);");               
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, id_editora_fk, id_categoria_fk) VALUES ('Poder do Hábito', 2012, 4, 4);");   
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, id_editora_fk, id_categoria_fk) VALUES ('O Hobbit', 1937, 2, 2);");            

        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('João Silva', 'joao.silva@email.com', 'Rua A, 123');"); 
        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('Maria Souza', 'maria.souza@email.com', 'Av B, 456');");  
        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('Pedro Santos', 'pedro.santos@email.com', 'Rua C, 789');"); 
        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('Ana Pereira', 'ana.pereira@email.com', 'Travessa D, 101');"); 

        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (1, 1);"); 
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (2, 2);"); 
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (3, 3);"); 
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (4, 4);"); 
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (5, 2);"); 

        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (1, 'Disponível');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (1, 'Disponível');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (2, 'Disponível');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (2, 'Disponível');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (2, 'Emprestado');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (3, 'Disponível');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (4, 'Disponível');"); 
        stmt.executeUpdate("INSERT INTO exemplar (id_livro_fk, status) VALUES (5, 'Disponível');"); 

        String activeLoanDate = LocalDate.now().minusDays(10).toString();
        String activeLoanDue = LocalDate.now().plusDays(5).toString();

        String overdueLoanDate = LocalDate.now().minusDays(25).toString();
        String overdueLoanDue = LocalDate.now().minusDays(10).toString();

        stmt.executeUpdate("INSERT INTO emprestimo (id_exemplar_fk, id_usuario_fk, data_emprestimo, data_prevista_devolucao) VALUES (1, 1, '" + activeLoanDate + "', '" + activeLoanDue + "');");
        stmt.executeUpdate("INSERT INTO emprestimo (id_exemplar_fk, id_usuario_fk, data_emprestimo, data_prevista_devolucao) VALUES (5, 2, '" + overdueLoanDate + "', '" + overdueLoanDue + "');");

        stmt.executeUpdate("INSERT INTO reserva (id_livro_fk, id_usuario_fk, data_reserva) VALUES (3, 3, '" + LocalDate.now().toString() + "');");
    }
}