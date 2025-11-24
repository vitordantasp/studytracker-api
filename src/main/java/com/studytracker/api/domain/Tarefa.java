package com.studytracker.api.domain;

import java.time.LocalDate;

public class Tarefa {
    
    private String titulo;
    private LocalDate prazo;
    private StatusTarefa status;
    
    public Tarefa(String titulo, LocalDate prazo) {
        this.titulo = titulo;
        this.prazo = prazo;
        this.status = StatusTarefa.PENDENTE; 
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public StatusTarefa getStatus() {
        return status;
    }
    
    public void setStatus(StatusTarefa status) {
        this.status = status;
    }
}
