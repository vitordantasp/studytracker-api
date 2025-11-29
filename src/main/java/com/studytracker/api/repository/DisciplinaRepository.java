package com.studytracker.api.repository;

import com.studytracker.api.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

// <Tipo da Entidade, Tipo do ID>
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    // Só isso! O Spring faz o resto.
}