package com.biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void create(Livro livro) {
        String sql = "INSERT INTO livro (titulo, ano_publicacao, editora) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, livro.getTitulo());
            pstmt.setInt(2, livro.getAnoPublicacao());
            pstmt.setString(3, livro.getEditora());
            pstmt.executeUpdate();
            System.out.println("Livro cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    public Livro read(int id) {
        String sql = "SELECT * FROM livro WHERE id_livro = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("editora")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar livro: " + e.getMessage());
        }
        return null;
    }

    public List<Livro> readAll() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livros.add(new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("editora")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os livros: " + e.getMessage());
        }
        return livros;
    }

    public void update(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, ano_publicacao = ?, editora = ? WHERE id_livro = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, livro.getTitulo());
            pstmt.setInt(2, livro.getAnoPublicacao());
            pstmt.setString(3, livro.getEditora());
            pstmt.setInt(4, livro.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro atualizado com sucesso.");
            } else {
                System.out.println("Livro não encontrado para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro removido com sucesso.");
            } else {
                System.out.println("Livro não encontrado para remoção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro: " + e.getMessage());
        }
    }
}