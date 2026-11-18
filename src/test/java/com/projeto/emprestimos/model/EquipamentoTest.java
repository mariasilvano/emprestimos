package com.projeto.emprestimos.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipamentoTest {

    @Test
    void emprestarEquipamentoMudaParaIndisponivel(){
        Equipamento equipamento = new Equipamento();

        equipamento.emprestar();

        assertFalse(equipamento.isDisponivel());
    }

    @Test
    void emprestarUmEquipamentoIndisponiveLancaExcecao(){
        Equipamento equipamento = new Equipamento();
        equipamento.emprestar();

        assertThrows(IllegalStateException.class, () -> equipamento.emprestar());
    }

    @Test
    void devolverMudaParaDisponivel(){
        Equipamento equipamento = new Equipamento();
        equipamento.emprestar();
        equipamento.devolver();

        assertTrue(equipamento.isDisponivel());
    }
}