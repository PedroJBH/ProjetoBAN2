package com.biblioteca;

import java.sql.*;
import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmprestimoDAO {

    private final ExemplarDAO exemplarDAO = new ExemplarDAO();
    private final LivroDAO livroDAO = new LivroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();

    public void realizarEmprestimo(int idLivro, int idUsuario) {
        String sql = "INSERT INTO emprestimo (id_exemplar_fk, id_usuario_fk, data_emprestimo, data_prevista_devolucao) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Exemplar exemplarDisponivel = exemplarDAO.buscarExemplarDisponivel(idLivro);
            if (exemplarDisponivel != null) {
                exemplarDisponivel.setStatus("Emprestado");
                exemplarDAO.update(exemplarDisponivel);

                pstmt.setInt(1, exemplarDisponivel.getId());
                pstmt.setInt(2, idUsuario);
                pstmt.setString(3, LocalDate.now().toString());
                pstmt.setString(4, LocalDate.now().plusDays(15).toString());
                pstmt.executeUpdate();
                System.out.println("Empréstimo realizado com sucesso. Exemplar ID: " + exemplarDisponivel.getId());
            } else {

                System.out.println("Não há exemplares disponíveis para este livro. Deseja fazer uma reserva? (s/n)");
                Scanner scanner = new Scanner(System.in);
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("s")) {
                    Livro livro = livroDAO.read(idLivro);
                    Usuario usuario = usuarioDAO.read(idUsuario);
                    if (livro != null && usuario != null) {
                        Reserva reserva = new Reserva(0, livro, usuario, Date.valueOf(LocalDate.now()));
                        reservaDAO.create(reserva);
                    }
                } else {
                    System.out.println("Operação de empréstimo cancelada.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
        }
    }

    public void devolverLivro(int idEmprestimo) {
        String sql = "UPDATE emprestimo SET data_devolucao = ? WHERE id_emprestimo = ?";
        String findExemplarSql = "SELECT id_exemplar_fk FROM emprestimo WHERE id_emprestimo = ?";
        
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            
            int idExemplar = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(findExemplarSql)) {
                pstmt.setInt(1, idEmprestimo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idExemplar = rs.getInt("id_exemplar_fk");
                }
            }
            
            if (idExemplar != -1) {
                Exemplar exemplar = exemplarDAO.read(idExemplar);
                if (exemplar != null) {
                    exemplar.setStatus("Disponível");
                    exemplarDAO.update(exemplar);
                }
            } else {
                System.out.println("Empréstimo não encontrado.");
                conn.rollback();
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, LocalDate.now().toString());
                pstmt.setInt(2, idEmprestimo);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Livro devolvido com sucesso.");
                }
            }
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erro ao devolver livro: " + e.getMessage());
        }
    }
    
    public void renovarEmprestimo(int idEmprestimo) {
        String sql = "UPDATE emprestimo SET data_prevista_devolucao = ? WHERE id_emprestimo = ? AND data_devolucao IS NULL";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String currentDueDateSql = "SELECT data_prevista_devolucao, id_exemplar_fk FROM emprestimo WHERE id_emprestimo = ?";
            LocalDate currentDueDate = null;
            int idExemplar = -1;
            
            try (PreparedStatement currentPstmt = conn.prepareStatement(currentDueDateSql)) {
                currentPstmt.setInt(1, idEmprestimo);
                ResultSet rs = currentPstmt.executeQuery();
                if (rs.next()) {
                    currentDueDate = LocalDate.parse(rs.getString("data_prevista_devolucao"));
                    idExemplar = rs.getInt("id_exemplar_fk");
                }
            }
            
            if (currentDueDate != null) {
                int idLivro = exemplarDAO.read(idExemplar).getLivro().getId();
                List<Reserva> reservasParaLivro = reservaDAO.readAll(); 
                
                boolean temReserva = false;
                for (Reserva r : reservasParaLivro) {
                    if (r.getLivro().getId() == idLivro) {
                        temReserva = true;
                        break;
                    }
                }

                if (temReserva) {
                    System.out.println("Não foi possível renovar. Existem reservas para este livro.");
                } else {
                    LocalDate newDueDate = currentDueDate.plusDays(15);
                    pstmt.setString(1, newDueDate.toString());
                    pstmt.setInt(2, idEmprestimo);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Empréstimo renovado com sucesso! Nova data de devolução: " + newDueDate);
                    } else {
                        System.out.println("Empréstimo não encontrado ou já devolvido.");
                    }
                }
            } else {
                System.out.println("Empréstimo não encontrado para renovação.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao renovar empréstimo: " + e.getMessage());
        }
    }

    public List<Emprestimo> relatorioEmprestimosAtivos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo WHERE data_devolucao IS NULL";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(
                    rs.getInt("id_emprestimo"),
                    rs.getInt("id_exemplar_fk"),
                    rs.getInt("id_usuario_fk"),
                    Date.valueOf(rs.getString("data_emprestimo")),
                    Date.valueOf(rs.getString("data_prevista_devolucao")),
                    (rs.getString("data_devolucao") != null) ? Date.valueOf(rs.getString("data_devolucao")) : null
                );
                emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório de empréstimos ativos: " + e.getMessage());
        }
        return emprestimos;
    }

    public List<Usuario> relatorioUsuariosComAtraso() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM usuario u JOIN emprestimo e ON u.id_usuario = e.id_usuario_fk WHERE e.data_devolucao IS NULL AND e.data_prevista_devolucao < ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDate.now().toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("endereco")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório de usuários com atraso: " + e.getMessage());
        }
        return usuarios;
    }
}