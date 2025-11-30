package com.studytracker.api.controller;

import com.studytracker.api.domain.Disciplina;
import com.studytracker.api.domain.Tarefa;
import com.studytracker.api.repository.DisciplinaRepository;
import com.studytracker.api.repository.TarefaRepository;
import com.studytracker.api.service.DisciplinaService;
import com.studytracker.api.service.TarefaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.studytracker.api.dto.DisciplinaRequestDTO;
import com.studytracker.api.dto.DisciplinaResponseDTO;
import com.studytracker.api.dto.TarefaRequestDTO;
import com.studytracker.api.dto.TarefaResponseDTO;

// 1. O "Crachá" de Garçom: Avisa ao Spring que essa classe controla rotas web
@RestController
// 2. A "Mesa" que ele atende: Qualquer URL que comece com /disciplinas vem pra cá
@RequestMapping("/disciplinas")
public class DisciplinaController {

    // 1. Declaramos quem a gente precisa (Dependências)
    private final DisciplinaService disciplinaService; // <--- O Chef novo      
    private final TarefaService tarefaService; // Precisa do TarefaService para criar tarefas

    public DisciplinaController(DisciplinaService disciplinaService, TarefaService tarefaService) {
        this.disciplinaService = disciplinaService;
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDTO>> listarTodas() {
        // O Controller não sabe mais de stream ou map. Ele só pede a lista pronta.
        List<DisciplinaResponseDTO> lista = disciplinaService.listarTodas();
        return ResponseEntity.ok(lista);
    }

   @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> criar(@RequestBody DisciplinaRequestDTO dados) {
        // O Controller não sabe instanciar Disciplina. Ele só repassa o pedido.
        DisciplinaResponseDTO novaDisciplina = disciplinaService.criar(dados);
        return ResponseEntity.status(201).body(novaDisciplina); // 201 Created é mais bonito que 200 OK para criação
    }
    
    @PostMapping("/{id}/tarefas")
    public ResponseEntity<TarefaResponseDTO> adicionarTarefa(@PathVariable Long id, @RequestBody TarefaRequestDTO dados) {
        try {
            var tarefa = tarefaService.criar(id, dados);
            return ResponseEntity.status(201).body(tarefa);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            // Agora chamamos o Service
            disciplinaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
