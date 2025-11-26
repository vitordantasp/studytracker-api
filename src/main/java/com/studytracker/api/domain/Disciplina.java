package com.studytracker.api.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Vira tabela
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String professor;

    // 6. O RELACIONAMENTO INVERSO
    // mappedBy = "disciplina": Diz que quem manda na relação é o campo 'disciplina' lá na classe Tarefa.
    // cascade = ALL: Se eu deletar a Disciplina, delete todas as Tarefas dela automaticamente.
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Tarefa> tarefas = new ArrayList<>();

    public Disciplina() { }

    public Disciplina(String nome, String professor) {
        this.nome = nome;
        this.professor = professor;
    }

    public void adicionarTarefa(Tarefa tarefa) {
        // Agora precisamos vincular os dois lados!
        tarefa.setDisciplina(this); // A tarefa precisa saber quem é o pai
        this.tarefas.add(tarefa);   // O pai adiciona o filho na lista
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    public List<Tarefa> getTarefas() { return tarefas; }
    public void setTarefas(List<Tarefa> tarefas) { this.tarefas = tarefas; }
}