package com.biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    // CREATE
    public void create(Autor autor) {
        String sql = "INSERT INTO autor (nome) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNome());
            pstmt.executeUpdate();
            System.out.println("Autor cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar autor: " + e.getMessage());
        }
    }

    // READ
    public Autor read(int id) {
        String sql = "SELECT * FROM autor WHERE id_autor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("nome")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar autor: " + e.getMessage());
        }
        return null;
    }

    public List<Autor> readAll() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autor";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                autores.add(new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("nome")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os autores: " + e.getMessage());
        }
        return autores;
    }

    // UPDATE
    public void update(Autor autor) {
        String sql = "UPDATE autor SET nome = ? WHERE id_autor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNome());
            pstmt.setInt(2, autor.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Autor atualizado com sucesso.");
            } else {
                System.out.println("Autor não encontrado para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar autor: " + e.getMessage());
        }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM autor WHERE id_autor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Autor removido com sucesso.");
            } else {
                System.out.println("Autor não encontrado para remoção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover autor: " + e.getMessage());
        }
    }

    // Relatório: Livros por Autor
    public List<Livro> relatorioLivrosPorAutor(int idAutor) {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.* FROM livro l JOIN livro_autor la ON l.id_livro = la.id_livro_fk WHERE la.id_autor_fk = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAutor);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                livros.add(new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("editora")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório de livros por autor: " + e.getMessage());
        }
        return livros;
    }
}