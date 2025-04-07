package com.ifsul.web2primeiro.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfessorDTO(
    @NotBlank(message = "O nome é obrigatório.") String nome,
    @NotBlank(message = "O email é obrigatório.") @Email(message = "Email inválido.") String email
) {}