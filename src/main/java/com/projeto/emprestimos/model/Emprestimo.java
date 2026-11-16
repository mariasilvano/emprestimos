package com.projeto.emprestimos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long equipamentoId;

    private Long colaboradorId;

    private LocalDate dataEmprestimo;

    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    public Emprestimo (){

    }

    public Long getId() {
        return id;
    }

    public Long getEquipamentoId() {
        return equipamentoId;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public Emprestimo(Long equipamentoId, Long colaboradorId, LocalDate hoje){

    }

    public void devolver(LocalDate data){
        this.dataDevolucao = data;
    }

    public boolean estaAtrasadoEm(LocalDate data){
        return data.isAfter(this.dataPrevistaDevolucao);
    }

    public boolean isAtrasado(){
        return estaAtrasadoEm(LocalDate.now());
    }

}
