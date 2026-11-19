package com.projeto.emprestimos.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String usuario, @NotBlank String senha) {

}
