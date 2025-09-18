package com.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LivroAutorDAO {
    // Processo de Negócio: Adicionar Autor a um Livro
    public void adicionarAutorALivro(int idLivro, int idAutor) {
        String sql = "INSERT INTO livro_autor (id_livro_fk, id_autor_fk) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLivro);
            pstmt.setInt(2, idAutor);
            pstmt.executeUpdate();
            System.out.println("Autor adicionado ao livro com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar autor ao livro: " + e.getMessage());
        }
    }
}