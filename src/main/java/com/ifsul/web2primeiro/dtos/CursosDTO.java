package com.ifsul.web2primeiro.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.annotation.PostConstruct;

public class CursosDTO {
    private String nome;
    private String descricao;
    private Date dataInicio;
    private Date dataFim;
    private String dataInicioStr;
    private String dataFimStr;
    private String imagem;
    private Integer categoriaID;
    private Integer professorID;

    // Construtor vazio (OBRIGATÓRIO para Thymeleaf)
    public CursosDTO() {
    }

    // Getters e Setters (todos obrigatórios)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Integer getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Integer categoriaID) {
        this.categoriaID = categoriaID;
    }

    public Integer getProfessorID() {
        return professorID;
    }

    public void setProfessorID(Integer professorID) {
        this.professorID = professorID;
    }
     @PostConstruct
    public void init() {
        if (this.dataInicioStr != null && !this.dataInicioStr.isEmpty() && this.dataFimStr != null && !this.dataFimStr.isEmpty()) {
            try {
                this.dataInicio = new SimpleDateFormat("yyyy-MM-dd").parse(this.dataInicioStr);
                this.dataFim = new SimpleDateFormat("yyyy-MM-dd").parse(this.dataFimStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public String getDataInicioStr() {
        return dataInicioStr;
    }
    
    public void setDataInicioStr(String dataInicioStr) {
        this.dataInicioStr = dataInicioStr;
    }
    
    public String getDataFimStr() {
        return dataFimStr;
    }
    
    public void setDataFimStr(String dataFimStr) {
        this.dataFimStr = dataFimStr;
    }
}