package com.ifsul.web2primeiro.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Cursos")
public class Cursos implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int ID;
    @NotBlank
    private String nome;
    private String descricao;
    private Date dataInicio;
    private Date dataFim;
    private String imagem;
    private int categoriaID;
    private int professorID;
    public int getID() {
        return ID;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return descricao;
    }
    public Date getDataInicio() {
        return dataInicio;
    }
    public Date getDataFim() {
        return dataFim;
    }
    public int getCategoriaID() {
        return categoriaID;
    }
    public int getProfessorID() {
        return professorID;
    }
    public String getImagem() {
        return imagem;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public void setDescricao (String descricao) {
        this.descricao = descricao;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDataInicio (Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    public void setDataFim (Date dataFim) {
        this.dataFim = dataFim;
    }
    public void setImagem (String imagem) {
        this.imagem = imagem;
    }
    public void setProfessorID(int professorID) {
        this.professorID = professorID;
    }
    public void setCategoriaID(int categoriaID) {
        this.categoriaID = categoriaID;
    }
}
