package com.studytracker.api.controller;

import com.studytracker.api.domain.StatusTarefa;
import com.studytracker.api.dto.TarefaResponseDTO;
import com.studytracker.api.service.TarefaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas") // Agora as rotas começam com /tarefas
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    // Veja como a URL ficou limpa! Não precisa do ID da disciplina.
    // URL Final: PATCH /tarefas/1/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusTarefa novoStatus) {
        
        // Precisamos ajustar o Service para não exigir ID da disciplina (ver passo 2)
        try {
            var tarefa = tarefaService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(tarefa);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // URL Final: DELETE /tarefas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            tarefaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}