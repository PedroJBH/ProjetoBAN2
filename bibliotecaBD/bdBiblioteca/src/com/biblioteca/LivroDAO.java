package com.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private final EditoraDAO editoraDAO = new EditoraDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private List<Autor> loadAutores(Connection conn, int idLivro) throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT a.id_autor, a.nome FROM autor a JOIN livro_autor la ON a.id_autor = la.id_autor_fk WHERE la.id_livro_fk = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLivro);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    autores.add(new Autor(rs.getInt("id_autor"), rs.getString("nome")));
                }
            }
        }
        return autores;
    }

    public int create(Livro livro) {
        String sql = "INSERT INTO livro (titulo, ano_publicacao, id_editora_fk, id_categoria_fk) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, livro.getTitulo());
            pstmt.setInt(2, livro.getAnoPublicacao());
            pstmt.setInt(3, livro.getEditora().getId());
            pstmt.setInt(4, livro.getCategoria().getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println("Livro cadastrado com sucesso.");
                        return generatedKeys.getInt(1); 
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar livro: " + e.getMessage());
        }
        return -1; 
    }

    public Livro read(int id) {
        String sql = "SELECT * FROM livro WHERE id_livro = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Editora editora = editoraDAO.read(rs.getInt("id_editora_fk"));
                Categoria categoria = categoriaDAO.read(rs.getInt("id_categoria_fk"));

                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getInt("ano_publicacao"),
                        editora,
                        categoria
                );
                livro.setAutores(loadAutores(conn, livro.getId())); 
                return livro;
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
                Editora editora = editoraDAO.read(rs.getInt("id_editora_fk"));
                Categoria categoria = categoriaDAO.read(rs.getInt("id_categoria_fk"));

                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getInt("ano_publicacao"),
                        editora,
                        categoria
                );

                livro.setAutores(loadAutores(conn, livro.getId())); 
                livros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os livros: " + e.getMessage());
        }
        return livros;
    }

    public void update(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, ano_publicacao = ?, id_editora_fk = ?, id_categoria_fk = ? WHERE id_livro = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, livro.getTitulo());
            pstmt.setInt(2, livro.getAnoPublicacao());
            pstmt.setInt(3, livro.getEditora().getId());
            pstmt.setInt(4, livro.getCategoria().getId());
            pstmt.setInt(5, livro.getId());
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