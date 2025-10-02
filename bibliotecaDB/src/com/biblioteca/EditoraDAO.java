package com.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditoraDAO {

    public void create(Editora editora) {
        String sql = "INSERT INTO editora (nome) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, editora.getNome());
            pstmt.executeUpdate();
            System.out.println("Editora cadastrada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar editora: " + e.getMessage());
        }
    }

    public Editora read(int id) {
        String sql = "SELECT id_editora, nome FROM editora WHERE id_editora = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Editora(rs.getInt("id_editora"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar editora por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Editora> readAll() {
        List<Editora> editoras = new ArrayList<>();
        String sql = "SELECT id_editora, nome FROM editora";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                editoras.add(new Editora(rs.getInt("id_editora"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar editoras: " + e.getMessage());
        }
        return editoras;
    }
    
    public void update(Editora editora) {
        String sql = "UPDATE editora SET nome = ? WHERE id_editora = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, editora.getNome());
            pstmt.setInt(2, editora.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Editora atualizada com sucesso.");
            } else {
                System.out.println("Editora não encontrada para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar editora: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM editora WHERE id_editora = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Editora removida com sucesso.");
            } else {
                System.out.println("Editora não encontrada para remoção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover editora: " + e.getMessage());
        }
    }
}