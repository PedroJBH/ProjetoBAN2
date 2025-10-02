package com.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExemplarDAO {
    private final LivroDAO livroDAO = new LivroDAO();

    public void create(Exemplar exemplar) {
        String sql = "INSERT INTO exemplar (id_livro_fk, status) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, exemplar.getLivro().getId());
            pstmt.setString(2, exemplar.getStatus());
            pstmt.executeUpdate();
            System.out.println("Exemplar cadastrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar exemplar: " + e.getMessage());
        }
    }

    public Exemplar read(int id) {
        String sql = "SELECT * FROM exemplar WHERE id_exemplar = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = livroDAO.read(rs.getInt("id_livro_fk"));
                    return new Exemplar(rs.getInt("id_exemplar"), livro, rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar exemplar: " + e.getMessage());
        }
        return null;
    }

    public List<Exemplar> readAll() {
        List<Exemplar> exemplares = new ArrayList<>();
        String sql = "SELECT * FROM exemplar";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = livroDAO.read(rs.getInt("id_livro_fk"));
                exemplares.add(new Exemplar(rs.getInt("id_exemplar"), livro, rs.getString("status")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar exemplares: " + e.getMessage());
        }
        return exemplares;
    }
    
    public void update(Exemplar exemplar) {
        String sql = "UPDATE exemplar SET status = ? WHERE id_exemplar = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exemplar.getStatus());
            pstmt.setInt(2, exemplar.getId());
            pstmt.executeUpdate();
            System.out.println("Exemplar atualizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar exemplar: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM exemplar WHERE id_exemplar = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Exemplar removido com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao remover exemplar: " + e.getMessage());
        }
    }

    public int contarExemplaresDisponiveis(int idLivro) {
        String sql = "SELECT COUNT(*) FROM exemplar WHERE id_livro_fk = ? AND status = 'Disponível'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLivro);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar exemplares disponíveis: " + e.getMessage());
        }
        return 0;
    }
    
    public Exemplar buscarExemplarDisponivel(int idLivro) {
        String sql = "SELECT * FROM exemplar WHERE id_livro_fk = ? AND status = 'Disponível' LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLivro);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = livroDAO.read(rs.getInt("id_livro_fk"));
                    return new Exemplar(rs.getInt("id_exemplar"), livro, rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar exemplar disponível: " + e.getMessage());
        }
        return null;
    }
}