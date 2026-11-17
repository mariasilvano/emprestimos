package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.service.EmprestimoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService service;

    public EmprestimoController(EmprestimoService service){
        this.service = service;
    }

    //TODO: listar, buscar por id e devolver.
}
