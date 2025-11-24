package com.studytracker.api.controller;

import com.studytracker.api.domain.Disciplina;
import org.springframework.web.bind.annotation.GetMapping;
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
            Disciplina d1 = new Disciplina("Java Web", "Prof. Josh");
            // Setamos o ID e aumentamos o contador
            d1.setId(proximoId++); 
            disciplinas.add(d1);

            Disciplina d2 = new Disciplina("Estrutura de Dados", "Prof. Knuth");
            d2.setId(proximoId++);
            disciplinas.add(d2);
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
}
