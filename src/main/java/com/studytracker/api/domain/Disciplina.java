package com.studytracker.api.domain;

import java.util.List;
import java.util.ArrayList;

public class Disciplina {

    private Long id; 
    private String nomeDisciplina;
    private String professor;
    private List<Tarefa> tarefas;
 
    // O Jackson precisa desse cosntrutor para instanciar o objeto antes de preencher
    public Disciplina() {
        this.tarefas = new ArrayList<>(); // Boa prática inicializar a lista
    }

    public Disciplina(String nomeDisciplina, String professor) {
        this.nomeDisciplina = nomeDisciplina;
        this.professor = professor;
        this.tarefas = new ArrayList<>(); 
    }
    
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public String getProfessor() {
        return professor;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public Long getId() {
        return id;
    }
    
    // O Jackson usa esses setters para injetar o valor que veio do JSON
    public void setNome(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
    }
}
