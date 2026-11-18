package com.projeto.emprestimos.service;


import com.projeto.emprestimos.dto.EmprestimoRequest;
import com.projeto.emprestimos.exception.ConflitoException;
import com.projeto.emprestimos.dto.EmprestimoResponse;
import com.projeto.emprestimos.exception.NaoEncontradoException;
import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final EquipamentoService equipamentoService;
    private final ColaboradorService colaboradorService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, EquipamentoService equipamentoService, ColaboradorService colaboradorService){
        this.emprestimoRepository = emprestimoRepository;
        this.equipamentoService = equipamentoService;
        this.colaboradorService = colaboradorService;
    }

    public List<Emprestimo> listar(Boolean atrasados){
        if(Boolean.TRUE.equals(atrasados)){
            return emprestimoRepository.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
        }
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscar(Long id){
        return emprestimoRepository.findById(id).orElseThrow(()-> new NaoEncontradoException("Empréstimo não encontrado!"));
    }

    public List<Emprestimo> listarDoColaborador(Long colabordorId){
        colaboradorService.buscar(colabordorId);
        return emprestimoRepository.findByColaboradorId(colabordorId);
    }

    @Transactional
    public EmprestimoResponse emprestar(EmprestimoRequest pedido) {
        Equipamento equipamento = equipamentoService.buscar(pedido.equipamentoId());
        Colaborador colaborador = colaboradorService.buscar(pedido.colaboradorId());

        if (emprestimoRepository.existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(
                colaborador.getId(), LocalDate.now())) {
            throw new ConflitoException("Colaborador com empréstimo atrasado");
        }

        equipamento.emprestar();

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.now());
        Emprestimo salvo =  emprestimoRepository.save(emprestimo);

        return new EmprestimoResponse(
                salvo.getId(),
                salvo.getEquipamento().getNome(),
                salvo.getColaborador().getNome(),
                salvo.getDataPrevistaDevolucao()
        );
    }

    @Transactional
    public Emprestimo devolver(Long id){
        Emprestimo emprestimo = buscar(id);

        LocalDate hoje = LocalDate.now();
        emprestimo.devolver(hoje);
        emprestimo.getEquipamento().devolver();

        return emprestimoRepository.save(emprestimo);
    }
}
