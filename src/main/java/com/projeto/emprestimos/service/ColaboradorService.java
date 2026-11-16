package com.projeto.emprestimos.service;

import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.repository.ColaboradorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository){
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<Colaborador> listar(){
        return colaboradorRepository.findAll();
    }

    public Colaborador buscar(Long id){
        return colaboradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }

    public Colaborador cadastrar(Colaborador colaborador){

        if(colaboradorRepository.existsByEmail(colaborador.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastradp!");
        }

        return colaboradorRepository.save(colaborador);
    }
}
