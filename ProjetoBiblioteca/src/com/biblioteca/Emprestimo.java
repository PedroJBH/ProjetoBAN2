package com.biblioteca;

import java.sql.Date;

public class Emprestimo {
    private int id;
    private int idLivro;
    private int idUsuario;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;

    public Emprestimo(int id, int idLivro, int idUsuario, Date dataEmprestimo, Date dataDevolucaoPrevista, Date dataDevolucaoReal) {
        this.id = id;
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdLivro() { return idLivro; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(Date dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public Date getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
    public Date getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(Date dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", idLivro=" + idLivro +
                ", idUsuario=" + idUsuario +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
                ", dataDevolucaoReal=" + dataDevolucaoReal +
                '}';
    }
}