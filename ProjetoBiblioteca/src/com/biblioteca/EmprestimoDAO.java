package com.biblioteca;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public void realizarEmprestimo(int idLivro, int idUsuario) {
        String sql = "INSERT INTO emprestimo (id_livro_fk, id_usuario_fk, data_emprestimo, data_devolucao_prevista) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLivro);
            pstmt.setInt(2, idUsuario);
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.setDate(4, Date.valueOf(LocalDate.now().plusDays(15))); // Prazo de 15 dias
            pstmt.executeUpdate();
            System.out.println("Empréstimo realizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
        }
    }

    public void devolverLivro(int idEmprestimo) {
        String sql = "UPDATE emprestimo SET data_devolucao_real = ? WHERE id_emprestimo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, idEmprestimo);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro devolvido com sucesso.");
            } else {
                System.out.println("Empréstimo não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao devolver livro: " + e.getMessage());
        }
    }

    public List<Emprestimo> relatorioEmprestimosAtivos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo WHERE data_devolucao_real IS NULL";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                emprestimos.add(new Emprestimo(
                        rs.getInt("id_emprestimo"),
                        rs.getInt("id_livro_fk"),
                        rs.getInt("id_usuario_fk"),
                        rs.getDate("data_emprestimo"),
                        rs.getDate("data_devolucao_prevista"),
                        rs.getDate("data_devolucao_real")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório de empréstimos ativos: " + e.getMessage());
        }
        return emprestimos;
    }

    public List<Usuario> relatorioUsuariosComAtraso() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM usuario u JOIN emprestimo e ON u.id_usuario = e.id_usuario_fk WHERE e.data_devolucao_real IS NULL AND e.data_devolucao_prevista < CURRENT_DATE";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("endereco")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório de usuários com atraso: " + e.getMessage());
        }
        return usuarios;
    }
}