package com.projeto.emprestimos.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Equipamento equipamento;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Colaborador colaborador;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false)
    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    public Emprestimo (){

    }

    public Long getId() {
        return id;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public Colaborador getColaborador() {
        return colaborador;
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

    public Emprestimo(Equipamento equipamento, Colaborador colaborador,
                      LocalDate hoje, int prazoEmDias) {
        this.equipamento = equipamento;
        this.colaborador = colaborador;
        this.dataEmprestimo = hoje;
        this.dataPrevistaDevolucao = hoje.plusDays(prazoEmDias);
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
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
