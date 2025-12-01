package com.studytracker.api.controller;

import com.studytracker.api.service.DisciplinaService;
import com.studytracker.api.service.TarefaService;
import com.studytracker.api.dto.DisciplinaRequestDTO;
import com.studytracker.api.dto.DisciplinaResponseDTO;
import com.studytracker.api.dto.TarefaRequestDTO;
import com.studytracker.api.dto.TarefaResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // 1. O "Crachá" de Garçom: Avisa ao Spring que essa classe controla rotas web
@RequestMapping("/disciplinas") // 2. A "Mesa" que ele atende: Qualquer URL que comece com /disciplinas vem pra cá
public class DisciplinaController {

    private final DisciplinaService disciplinaService;     
    private final TarefaService tarefaService; // Precisa do TarefaService para criar tarefas

    public DisciplinaController(DisciplinaService disciplinaService, TarefaService tarefaService) {
        this.disciplinaService = disciplinaService;
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDTO>> listarTodas() {
        List<DisciplinaResponseDTO> lista = disciplinaService.listarTodasDisciplinas();
        return ResponseEntity.ok(lista);
    }

   @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> criarDisciplina(@RequestBody DisciplinaRequestDTO dados) {
        DisciplinaResponseDTO novaDisciplina = disciplinaService.adicionarDisciplina(dados);
        return ResponseEntity.status(201).body(novaDisciplina); // 201 Created é mais bonito que 200 OK para criação
    }
    
    @PostMapping("/{id}/tarefas")
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@PathVariable Long id, @RequestBody TarefaRequestDTO dados) {
        var tarefa = tarefaService.adicionarTarefa(id, dados);
        return ResponseEntity.status(201).body(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerDisciplina(@PathVariable Long id) {
        disciplinaService.deletarDisciplina(id);
        return ResponseEntity.noContent().build();
    }
        
}
