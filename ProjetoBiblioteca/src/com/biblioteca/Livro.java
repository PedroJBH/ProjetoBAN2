package com.biblioteca;

public class Livro {
    private int id;
    private String titulo;
    private int anoPublicacao;
    private String editora;

    public Livro(int id, String titulo, int anoPublicacao, String editora) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", editora='" + editora + '\'' +
                '}';
    }
}