package com.projeto.emprestimos.repository;

import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.model.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    boolean existsByPatrimonio(String patrimonio);

    List<Equipamento> findByTipo(TipoEquipamento tipo);

    List<Equipamento>findByDisponivel(boolean disponivel);

    List<Equipamento>findByTipoAndDisponivel(TipoEquipamento tipo, boolean disponivel);
}
