package com.ifsul.web2primeiro.dtos;

import jakarta.validation.constraints.NotBlank;

public record CursosDTO(@NotBlank String nome, @NotBlank String descricao, @NotBlank String dataInicio, @NotBlank String dataFinal, @NotBlank String imagem, @NotBlank Integer professorId, @NotBlank Integer categoriaId) {

}