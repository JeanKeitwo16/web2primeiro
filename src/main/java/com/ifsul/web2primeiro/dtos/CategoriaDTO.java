package com.ifsul.web2primeiro.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(@NotBlank String nome) {

}
