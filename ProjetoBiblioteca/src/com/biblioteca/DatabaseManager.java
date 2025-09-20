package com.biblioteca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:biblioteca.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS emprestimo;");
            stmt.execute("DROP TABLE IF EXISTS livro_autor;");
            stmt.execute("DROP TABLE IF EXISTS livro;");
            stmt.execute("DROP TABLE IF EXISTS autor;");
            stmt.execute("DROP TABLE IF EXISTS usuario;");

            stmt.execute("CREATE TABLE autor (" +
                    "id_autor INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome VARCHAR(255) NOT NULL" +
                    ");");

            stmt.execute("CREATE TABLE livro (" +
                    "id_livro INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo VARCHAR(255) NOT NULL," +
                    "ano_publicacao INTEGER NOT NULL," +
                    "editora VARCHAR(255) NOT NULL" +
                    ");");

            stmt.execute("CREATE TABLE usuario (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL UNIQUE," +
                    "endereco VARCHAR(255) NOT NULL" +
                    ");");

            stmt.execute("CREATE TABLE livro_autor (" +
                    "id_livro_fk INTEGER," +
                    "id_autor_fk INTEGER," +
                    "PRIMARY KEY (id_livro_fk, id_autor_fk)," +
                    "FOREIGN KEY (id_livro_fk) REFERENCES livro(id_livro) ON DELETE CASCADE," +
                    "FOREIGN KEY (id_autor_fk) REFERENCES autor(id_autor) ON DELETE CASCADE" +
                    ");");

            stmt.execute("CREATE TABLE emprestimo (" +
                    "id_emprestimo INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_livro_fk INTEGER," +
                    "id_usuario_fk INTEGER," +
                    "data_emprestimo DATE NOT NULL," +
                    "data_devolucao_prevista DATE NOT NULL," +
                    "data_devolucao_real DATE," +
                    "FOREIGN KEY (id_livro_fk) REFERENCES livro(id_livro) ON DELETE CASCADE," +
                    "FOREIGN KEY (id_usuario_fk) REFERENCES usuario(id_usuario) ON DELETE CASCADE" +
                    ");");

            insertInitialData(stmt);

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    private static void insertInitialData(Statement stmt) throws SQLException {

        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('George Orwell');");
        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('J.R.R. Tolkien');");
        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('Isaac Asimov');");
        stmt.executeUpdate("INSERT INTO autor (nome) VALUES ('Mary Shelley');");

        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, editora) VALUES ('1984', 1949, 'Secker & Warburg');");
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, editora) VALUES ('O Senhor dos Anéis', 1954, 'Allen & Unwin');");
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, editora) VALUES ('Eu, Robô', 1950, 'Gnome Press');");
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, editora) VALUES ('Fundação', 1951, 'Gnome Press');");
        stmt.executeUpdate("INSERT INTO livro (titulo, ano_publicacao, editora) VALUES ('Frankenstein', 1818, 'Lackington, Hughes, Harding, Mavor & Jones');");

        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('João Silva', 'joao.silva@email.com', 'Rua A, 123');");
        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('Maria Souza', 'maria.souza@email.com', 'Av B, 456');");
        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('Pedro Santos', 'pedro.santos@email.com', 'Rua C, 789');");
        stmt.executeUpdate("INSERT INTO usuario (nome, email, endereco) VALUES ('Ana Pereira', 'ana.pereira@email.com', 'Travessa D, 101');");

        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (1, 1);"); // 1984 - Orwell
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (2, 2);"); // O Senhor dos Anéis - Tolkien
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (3, 3);"); // Eu, Robô - Asimov
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (4, 3);"); // Fundação - Asimov
        stmt.executeUpdate("INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (5, 4);"); // Frankenstein - Shelley

        stmt.executeUpdate("INSERT INTO emprestimo (id_livro_fk, id_usuario_fk, data_emprestimo, data_devolucao_prevista) VALUES (1, 1, '2025-08-01', '2025-08-15');");

        stmt.executeUpdate("INSERT INTO emprestimo (id_livro_fk, id_usuario_fk, data_emprestimo, data_devolucao_prevista) VALUES (2, 2, '2025-07-20', '2025-08-03');");

        stmt.executeUpdate("INSERT INTO emprestimo (id_livro_fk, id_usuario_fk, data_emprestimo, data_devolucao_prevista, data_devolucao_real) VALUES (3, 3, '2025-07-10', '2025-07-25', '2025-07-22');");
    }
}