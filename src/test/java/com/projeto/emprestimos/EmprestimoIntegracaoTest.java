package com.projeto.emprestimos;

import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.model.TipoEquipamento;
import com.projeto.emprestimos.repository.ColaboradorRepository;
import com.projeto.emprestimos.repository.EmprestimoRepository;
import com.projeto.emprestimos.repository.EquipamentoRepository;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class EmprestimoIntegracaoTest {

    @Autowired MockMvc mockMvc;
    @Autowired
    EquipamentoRepository equipamentoRepository;
    @Autowired
    ColaboradorRepository colaboradorRepository;
    @Autowired
    EmprestimoRepository emprestimoRepository;

    private Equipamento novoEquipamento(String patrimonio) {
        Equipamento e = new Equipamento();
        e.setPatrimonio(patrimonio);
        e.setNome("Notebook Teste");
        e.setTipo(TipoEquipamento.NOTEBOOK);
        return equipamentoRepository.save(e);
    }

    private Colaborador novoColaborador(String email) {
        Colaborador c = new Colaborador();
        c.setNome("Ana Teste");
        c.setEmail(email);
        return colaboradorRepository.save(c);
    }

    @Test
    void buscarEquipamentoInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/equipamentos/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastrarEquipamentoRetorna201() throws Exception {
        mockMvc.perform(post("/equipamentos").contentType(MediaType.APPLICATION_JSON).content("{\"patrimonio\":\"NB-900\",\"nome\":\"Notebook Dell\",\"tipo\":\"NOTEBOOK\"}")).andExpect(status().isCreated()).andExpect(jsonPath("$.disponivel").value(true));

    }

        @Test
        void emprestarEDevolverRetorna200 () throws Exception {
            Equipamento eq = novoEquipamento("NB-901");
            Colaborador col = novoColaborador("ana901@teste.com");

            mockMvc.perform(post("/emprestimos")
                            .param("equipamentoId", eq.getId().toString())
                            .param("colaboradorId", col.getId().toString()))
                    .andExpect(status().isCreated());

            Long emprestimoId = emprestimoRepository.findByColaboradorId(col.getId()).get(0).getId();

            mockMvc.perform(put("/emprestimos/{id}/devolucao", emprestimoId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.dataDevolucao").isNotEmpty());
        }

        @Test
        void emprestarEquipamentoJaEmprestadoRetorna409 () throws Exception {
            Equipamento eq = novoEquipamento("NB-902");
            Colaborador col = novoColaborador("ana902@teste.com");

            mockMvc.perform(post("/emprestimos")
                            .param("equipamentoId", eq.getId().toString())
                            .param("colaboradorId", col.getId().toString()))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/emprestimos")
                            .param("equipamentoId", eq.getId().toString())
                            .param("colaboradorId", col.getId().toString()))
                    .andExpect(status().isConflict());
        }
    }

