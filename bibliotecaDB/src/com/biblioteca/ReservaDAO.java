package com.biblioteca;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private final LivroDAO livroDAO = new LivroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void create(Reserva reserva) {
        String sql = "INSERT INTO reserva (id_livro_fk, id_usuario_fk, data_reserva) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getLivro().getId());
            pstmt.setInt(2, reserva.getUsuario().getId());
            pstmt.setString(3, reserva.getDataReserva().toString()); 
            pstmt.executeUpdate();
            System.out.println("Reserva realizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao realizar reserva: " + e.getMessage());
        }
    }

    public Reserva read(int id) {
        String sql = "SELECT * FROM reserva WHERE id_reserva = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = livroDAO.read(rs.getInt("id_livro_fk"));
                    Usuario usuario = usuarioDAO.read(rs.getInt("id_usuario_fk"));
                    return new Reserva(
                        rs.getInt("id_reserva"),
                        livro,
                        usuario,
                        Date.valueOf(rs.getString("data_reserva")) 
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar reserva: " + e.getMessage());
        }
        return null;
    }
    
    public List<Reserva> readAll() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = livroDAO.read(rs.getInt("id_livro_fk"));
                Usuario usuario = usuarioDAO.read(rs.getInt("id_usuario_fk"));
                reservas.add(new Reserva(
                    rs.getInt("id_reserva"),
                    livro,
                    usuario,
                    Date.valueOf(rs.getString("data_reserva")) 
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar reservas: " + e.getMessage());
        }
        return reservas;
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM reserva WHERE id_reserva = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reserva removida com sucesso.");
            } else {
                System.out.println("Reserva não encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover reserva: " + e.getMessage());
        }
    }
}