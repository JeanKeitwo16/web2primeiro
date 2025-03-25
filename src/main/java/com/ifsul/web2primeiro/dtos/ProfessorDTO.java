package com.ifsul.web2primeiro.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProfessorDTO(@NotBlank String nome, @NotBlank String email) {

}