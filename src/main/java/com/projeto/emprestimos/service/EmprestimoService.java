package com.projeto.emprestimos.service;


import com.projeto.emprestimos.exception.ConflitoException;
import com.projeto.emprestimos.exception.NaoEncontradoException;
import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.repository.EmprestimoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final EquipamentoService equipamentoService;
    private final ColaboradorService colaboradorService;
    private static final int PRAZO_EM_DIAS = 14;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, EquipamentoService equipamentoService, ColaboradorService colaboradorService){
        this.emprestimoRepository = emprestimoRepository;
        this.equipamentoService = equipamentoService;
        this.colaboradorService = colaboradorService;
    }

    public Emprestimo buscar(Long id){
        return emprestimoRepository.findById(id).orElseThrow(()-> new NaoEncontradoException("Empréstimo não encontrado!"));
    }

    public List<Emprestimo> listarDoColaborador(Long colabordorId){
        colaboradorService.buscar(colabordorId);
        return emprestimoRepository.findByColaboradorId(colabordorId);
    }

    @Transactional
    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId){
        Equipamento equipamento= equipamentoService.buscar(equipamentoId);
        Colaborador colaborador = colaboradorService.buscar(colaboradorId);

        if(!equipamento.isDisponivel()){//valida se o equipamento está disponível para realizar o empréstimo.
            throw new ConflitoException("Equipamento indisponível");
        }
        //logo após seta ele como indisponível
        equipamento.setDisponivel(false);

        LocalDate hoje = LocalDate.now();

        Emprestimo emprestimo = new Emprestimo(
                equipamento,
                colaborador,
                hoje,
                PRAZO_EM_DIAS
        );

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo devolver(Long id){
        Emprestimo emprestimo = buscar(id);

        if (emprestimo.getDataDevolucao() != null){
            throw new ConflitoException("Empréstimo já devolvido");
        }

        LocalDate hoje = LocalDate.now();
        emprestimo.devolver(hoje);
        emprestimo.getEquipamento().setDisponivel(true);

        return emprestimoRepository.save(emprestimo);
    }
}
