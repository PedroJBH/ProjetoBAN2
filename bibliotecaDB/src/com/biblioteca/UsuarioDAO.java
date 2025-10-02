package com.biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void create(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, endereco) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getEndereco());
            pstmt.executeUpdate();
            System.out.println("Usuário cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public Usuario read(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("endereco")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar usuário: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> readAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("endereco")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os usuários: " + e.getMessage());
        }
        return usuarios;
    }

    public void update(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, endereco = ? WHERE id_usuario = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getEndereco());
            pstmt.setInt(4, usuario.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Usuário atualizado com sucesso.");
            } else {
                System.out.println("Usuário não encontrado para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Usuário removido com sucesso.");
            } else {
                System.out.println("Usuário não encontrado para remoção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover usuário: " + e.getMessage());
        }
    }
}