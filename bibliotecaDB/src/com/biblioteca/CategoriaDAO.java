package com.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void create(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria.getNome());
            pstmt.executeUpdate();
            System.out.println("Categoria cadastrada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar categoria: " + e.getMessage());
        }
    }

    public Categoria read(int id) {
        String sql = "SELECT id_categoria, nome FROM categoria WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id_categoria"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Categoria> readAll() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nome FROM categoria";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }

    public void update(Categoria categoria) {
        String sql = "UPDATE categoria SET nome = ? WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria.getNome());
            pstmt.setInt(2, categoria.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Categoria atualizada com sucesso.");
            } else {
                System.out.println("Categoria não encontrada para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Categoria removida com sucesso.");
            } else {
                System.out.println("Categoria não encontrada para remoção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover categoria: " + e.getMessage());
        }
    }
}