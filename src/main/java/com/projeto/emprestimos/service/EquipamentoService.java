package com.projeto.emprestimos.service;

import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.model.TipoEquipamento;
import com.projeto.emprestimos.repository.EmprestimoRepository;
import com.projeto.emprestimos.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {
    private final EquipamentoRepository equipamentoRepository;
    private final EmprestimoRepository emprestimoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, EmprestimoRepository emprestimoRepository){
        this.equipamentoRepository = equipamentoRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public List<Equipamento> listar(TipoEquipamento tipo, Boolean disponivel){
        if (tipo != null && disponivel != null) {
            return equipamentoRepository.findByTipoAndDisponivel(tipo, disponivel);
        }

        if (tipo != null) {
            return equipamentoRepository.findByTipo(tipo);
        }

        if (disponivel != null) {
            return equipamentoRepository.findByDisponivel(disponivel);
        }

        return equipamentoRepository.findAll();
    }

    public Equipamento buscar(Long id){
        return equipamentoRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado!"));
    }

    public Equipamento cadastrar(Equipamento equipamento){
        if(equipamentoRepository.existsByPatrimonio(equipamento.getPatrimonio())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Patrimônio já cadastrado");
        }

        equipamento.setDisponivel(true);
        return equipamentoRepository.save(equipamento);
    }

    public Equipamento atualizar(Long id, Equipamento dados){
        Equipamento encontrado = buscar(id);//busca o equipamento que deve ser atualizado

        //modifica os dados do equipamento
        encontrado.setNome(dados.getNome());
        encontrado.setTipo(dados.getTipo());

        //salva os novos dados
        return equipamentoRepository.save(encontrado);
    }
}