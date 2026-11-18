package com.projeto.emprestimos.dto;

import java.time.LocalDate;

public record EmprestimoResponse(
        Long id,
        String equipamentoNome,
        String colaboradorNome,
        LocalDate dataPrevistaDevolucao) {

}
