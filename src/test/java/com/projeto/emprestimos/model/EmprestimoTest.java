package com.projeto.emprestimos.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    private Emprestimo criarEm(LocalDate data) {
        return new Emprestimo(new Equipamento(), new Colaborador(), data);
    }

    @Test
    void verificarDatasEmprestimo(){
        Emprestimo e = criarEm(LocalDate.of(2026, 11, 14));

        assertEquals(LocalDate.of(2026,11,14), e.getDataEmprestimo());

        assertEquals(LocalDate.of(2026,11,28), e.getDataPrevistaDevolucao());

    }

    @Test
    void devolverGuardaADataDeDevolucao() {                              // R7
        Emprestimo e = criarEm(LocalDate.of(2026, 11, 14));

        e.devolver(LocalDate.of(2026, 11, 20));

        assertEquals(LocalDate.of(2026, 11, 20), e.getDataDevolucao());
    }

    @Test
    void devolverDuasVezesLancaExcecao() {                               // R6
        Emprestimo e = criarEm(LocalDate.of(2026, 11, 14));
        e.devolver(LocalDate.of(2026, 11, 20));

        assertThrows(IllegalStateException.class,
                () -> e.devolver(LocalDate.of(2026, 11, 21)));
    }

    @Test
    void naoDevolvidoComPrazoVencidoEstaAtrasado() {                     // R8
        Emprestimo e = criarEm(LocalDate.of(2026, 10, 27));   // prazo 10/11

        assertTrue(e.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void naoDevolvidoDentroDoPrazoNaoEstaAtrasado() {
        Emprestimo e = criarEm(LocalDate.of(2026, 11, 6));    // prazo 20/11

        assertFalse(e.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void devolvidoComAtrasoNaoEstaAtrasado() {
        Emprestimo e = criarEm(LocalDate.of(2026, 10, 27));   // prazo 10/11
        e.devolver(LocalDate.of(2026, 11, 12));

        assertFalse(e.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }

    @Test
    void noDiaDoPrazoAindaNaoEstaAtrasado() {
        Emprestimo e = criarEm(LocalDate.of(2026, 10, 31));   // prazo 14/11

        assertFalse(e.estaAtrasadoEm(LocalDate.of(2026, 11, 14)));
    }
}
