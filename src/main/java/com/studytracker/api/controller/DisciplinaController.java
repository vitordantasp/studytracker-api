package com.studytracker.api.controller;

import com.studytracker.api.domain.Disciplina;
import com.studytracker.api.domain.StatusTarefa;
import com.studytracker.api.domain.Tarefa;
import com.studytracker.api.repository.DisciplinaRepository;
import com.studytracker.api.repository.TarefaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// 1. O "Crachá" de Garçom: Avisa ao Spring que essa classe controla rotas web
@RestController
// 2. A "Mesa" que ele atende: Qualquer URL que comece com /disciplinas vem pra cá
@RequestMapping("/disciplinas")
public class DisciplinaController {

    // 1. Declaramos quem a gente precisa (Dependências)
    private final DisciplinaRepository disciplinaRepository;
    private final TarefaRepository tarefaRepository;

    // 2. Injeção via Construtor
    // O Spring vê isso e diz: "Deixa comigo, vou passar as instâncias certas aqui".
    public DisciplinaController(DisciplinaRepository disciplinaRepo, TarefaRepository tarefaRepository) {
        this.disciplinaRepository = disciplinaRepo;
        this.tarefaRepository = tarefaRepository;
    }

    // 3. O Verbo HTTP: Quando chegar um pedido GET (leitura), execute isso
    @GetMapping
    public List<Disciplina> listarTodas() {
        // Antes: return disciplinas;
        // Agora: Select * from disciplina
        return disciplinaRepository.findAll();
    }

    // 4. O Verbo POST: Quando chegar um pedido de CRIAÇÃO
    @PostMapping
    public Disciplina criar(@RequestBody Disciplina novaDisciplina) {
        // Antes: disciplinas.add(novaDisciplina);
        // Agora: Insert into disciplina ...
        return disciplinaRepository.save(novaDisciplina);
    }
    
    @PostMapping("/{id}/tarefas")
    public ResponseEntity<Object> adicionarTarefa(@PathVariable Long id, @RequestBody Tarefa novaTarefa) {
        // 1. Buscamos no banco pelo ID (SELECT * FROM disciplina WHERE id = ?)
        Optional<Disciplina> disciplinaOpt = disciplinaRepository.findById(id);

        // 2. Verificamos se a caixa está vazia (Não achou)
        if (disciplinaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 3. Pegamos a disciplina de dentro da caixa
        Disciplina disciplina = disciplinaOpt.get();

        // 4. Adicionamos a tarefa (Lógica da classe de Domínio)
        disciplina.adicionarTarefa(novaTarefa);
        
        // 5. Salvamos a DISCIPLINA. 
        // Como configuramos 'CascadeType.ALL' na entidade, o JPA percebe a tarefa nova e salva ela também!
        disciplinaRepository.save(disciplina);

        return ResponseEntity.ok(disciplina);
    }

    @PatchMapping("/{idDisciplina}/tarefas/{idTarefa}/status")
    public ResponseEntity<Object> atualizarStatus(
            @PathVariable Long idDisciplina,
            @PathVariable Long idTarefa,
            @RequestBody StatusTarefa novoStatus) {

        // 1. Buscamos a Tarefa direto no banco (SELECT * FROM tarefa WHERE id = ?)
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(idTarefa);

        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tarefa tarefa = tarefaOpt.get();

        // 2. Validação de Segurança (Opcional mas recomendada):
        // Garantir que a tarefa realmente pertence à disciplina da URL
        if (!tarefa.getDisciplina().getId().equals(idDisciplina)) {
            return ResponseEntity.badRequest().body("Essa tarefa não pertence a essa disciplina!");
        }

        // 3. Atualizamos e Salvamos
        tarefa.setStatus(novoStatus);
        tarefaRepository.save(tarefa);

        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDisciplina(@PathVariable Long id) {
        
        // 1. Verificação rápida antes de tentar apagar
        if (!disciplinaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // 2. Apaga do banco
        disciplinaRepository.deleteById(id);

        // 3. Retorna 204 No Content
        // É o padrão mundial para "Apaguei com sucesso e não tenho nada pra te mostrar de volta"
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idDisciplina}/tarefas/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(
            @PathVariable Long idDisciplina,
            @PathVariable Long idTarefa) {

        // 1. Buscamos a tarefa no banco
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(idTarefa);

        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tarefa tarefa = tarefaOpt.get();

        // 2. Validação de Segurança: 
        // A tarefa existe, mas será que ela pertence à disciplina da URL?
        // Se a tarefa 10 pertence a Matematica (ID 1) e tentarem deletar via /disciplinas/2/tarefas/10, barramos.
        if (!tarefa.getDisciplina().getId().equals(idDisciplina)) {
            return ResponseEntity.badRequest().build();
        }

        // 3. Deletamos a tarefa
        tarefaRepository.delete(tarefa);

        // 4. Retornamos 204 No Content (Sucesso sem corpo)
        return ResponseEntity.noContent().build();
    }
}
