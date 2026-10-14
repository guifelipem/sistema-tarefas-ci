package br.com.faculdade.tarefas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TarefaApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TarefaRepository repository;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void limpar() {
        repository.deleteAll();
    }

    @Test
    void deveExecutarCrudCompleto() throws Exception {
        String json = mvc.perform(
                post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Estudar CI",
                                  "descricao": "Trabalho",
                                  "status": "PENDENTE"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Estudar CI"))
                .andExpect(jsonPath("$.dataCriacao").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = mapper.readTree(json).get("id").asLong();

        mvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(get("/api/tarefas/" + id))
                .andExpect(status().isOk());

        mvc.perform(
                put("/api/tarefas/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Apresentar CI",
                                  "status": "PENDENTE"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Apresentar CI"));

        mvc.perform(patch("/api/tarefas/" + id + "/concluir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));

        assertEquals(
                Tarefa.Status.CONCLUIDA,
                repository.findById(id).orElseThrow().getStatus()
        );

        mvc.perform(delete("/api/tarefas/" + id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/tarefas/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRejeitarTituloVazio() throws Exception {
        mvc.perform(
                post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "  ",
                                  "status": "PENDENTE"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        assertEquals(0, repository.count());
    }

    @Test
    void deveRejeitarStatusInvalido() throws Exception {
        mvc.perform(
                post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "CI",
                                  "status": "INVALIDO"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoEditarAusente() throws Exception {
        mvc.perform(
                put("/api/tarefas/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "CI",
                                  "status": "PENDENTE"
                                }
                                """)
                )
                .andExpect(status().isOk());
    }
}
