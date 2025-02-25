package com.ifsul.web2primeiro.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Categoria")
public class Categorias implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int ID;
    @NotBlank
    private String nome;
    public int getID() {
        return ID;
    }
    public String getNome() {
        return nome;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}
