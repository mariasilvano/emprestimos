package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.service.ColaboradorService;
import com.projeto.emprestimos.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {
    private final ColaboradorService colaboradorService;
    private final EmprestimoService emprestimoService;

    public ColaboradorController(ColaboradorService service, EmprestimoService emprestimoService){
        this.colaboradorService = service;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public List<Colaborador> listar(){
        return colaboradorService.listar();
    }

    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Long id){
        return colaboradorService.buscar(id);
    }

    @GetMapping("/{id}/emprestimos")
    public List<Emprestimo> buscarEmprestimos(@PathVariable Long id){
        return emprestimoService.listarDoColaborador(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colaborador cadastrar(@RequestBody @Valid Colaborador colaborador){
        return colaboradorService.cadastrar(colaborador);
    }

}
