package com.studytracker.api.controller;

import com.studytracker.api.domain.Disciplina;
import com.studytracker.api.domain.StatusTarefa;
import com.studytracker.api.domain.Tarefa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody; 

import java.util.ArrayList;
import java.util.List;

// 1. O "Crachá" de Garçom: Avisa ao Spring que essa classe controla rotas web
@RestController
// 2. A "Mesa" que ele atende: Qualquer URL que comece com /disciplinas vem pra cá
@RequestMapping("/disciplinas")
public class DisciplinaController {

    // Simulação de Banco de Dados (igual fazíamos na Main antiga)
    private static List<Disciplina> disciplinas = new ArrayList<>();

    // Um contador simples para gerar IDs (1, 2, 3...)
    private static long proximoId = 1;

    // Construtor: Vamos criar dados falsos só pra testar se funciona
    public DisciplinaController() {
       if (disciplinas.isEmpty()) {
            // Criamos a disciplina
            Disciplina disciplina1 = new Disciplina("Java Web", "Prof. Josh");
            // Setamos o ID e aumentamos o contador
            disciplina1.setId(proximoId++); 
            disciplinas.add(disciplina1);

            Disciplina disciplina2 = new Disciplina("Estrutura de Dados", "Prof. Knuth");
            disciplina2.setId(proximoId++);
            disciplinas.add(disciplina2);
        }
    }

    // 3. O Verbo HTTP: Quando chegar um pedido GET (leitura), execute isso
    @GetMapping
    public List<Disciplina> listarTodas() {
        return disciplinas;
    }

    // 4. O Verbo POST: Quando chegar um pedido de CRIAÇÃO
    @PostMapping
    public Disciplina criar(@RequestBody Disciplina novaDisciplina) {
        // O Spring já criou o objeto 'novaDisciplina' com os dados do JSON que chegaram!
        
        // Antes de salvar na lista, a gente "carimba" o ID nela
        novaDisciplina.setId(proximoId++);
        
        // Adicionamos na nossa lista em memória
        disciplinas.add(novaDisciplina);
        
        // Retornamos o objeto criado para confirmar (padrão de API)
        return novaDisciplina;
    }

    // Mapeamos uma sub-rota.
    // Como a classe já tem @RequestMapping("/disciplinas"), aqui só colocamos o resto.
    @PostMapping("/{id}/tarefas")
    public ResponseEntity<Object> adicionarTarefa(@PathVariable Long id, @RequestBody Tarefa novaTarefa) {
        
        // 1. Procurar a disciplina pelo ID na nossa lista
        // (Lógica simples de busca linear, igual a do console)
        Disciplina disciplinaEncontrada = null;
        
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getId().equals(id)) {
                disciplinaEncontrada = disciplina;
                break;
            }
        }

        // 2. Cenário Triste: Não achou a disciplina?
        if (disciplinaEncontrada == null) {
            // Retorna um erro 404 (Not Found)
            return ResponseEntity.notFound().build();
        }

        // 3. Cenário Feliz: Achou!
        // Adicionamos a tarefa à disciplina
        disciplinaEncontrada.adicionarTarefa(novaTarefa);
        
        // Retornamos a disciplina atualizada (ou apenas a tarefa) com status 200 OK
        return ResponseEntity.ok(disciplinaEncontrada);
    }

    @PatchMapping("/{idDisciplina}/tarefas/{idTarefa}/status")
    public ResponseEntity<Object> atualizarStatus(
            @PathVariable Long idDisciplina,
            @PathVariable Long idTarefa,
            @RequestBody StatusTarefa novoStatus) { // O Spring converte o texto "CONCLUIDA" pro Enum!

        // 1. Achar a Disciplina
        Disciplina disciplinaEncontrada = null;
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getId().equals(idDisciplina)) {
                disciplinaEncontrada = disciplina;
                break;
            }
        }
        if (disciplinaEncontrada == null) return ResponseEntity.notFound().build();

        // 2. Achar a Tarefa DENTRO da disciplina
        Tarefa tarefaEncontrada = null;
        for (Tarefa tarefa : disciplinaEncontrada.getTarefas()) {
            if (tarefa.getId().equals(idTarefa)) {
                tarefaEncontrada = tarefa;
                break;
            }
        }
        if (tarefaEncontrada == null) return ResponseEntity.notFound().build();

        // 3. Atualizar
        tarefaEncontrada.setStatus(novoStatus);

        return ResponseEntity.ok(tarefaEncontrada);
    }
}
