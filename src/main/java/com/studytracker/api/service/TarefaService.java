package com.studytracker.api.service;

import com.studytracker.api.domain.Disciplina;
import com.studytracker.api.domain.StatusTarefa;
import com.studytracker.api.domain.Tarefa;
import com.studytracker.api.dto.TarefaRequestDTO;
import com.studytracker.api.dto.TarefaResponseDTO;
import com.studytracker.api.repository.DisciplinaRepository;
import com.studytracker.api.repository.TarefaRepository;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final DisciplinaRepository disciplinaRepository; // Precisamos para validar o pai

    public TarefaService(TarefaRepository tarefaRepository, DisciplinaRepository disciplinaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    // Movemos o criar para cá
    public TarefaResponseDTO criar(Long idDisciplina, TarefaRequestDTO dados) {
        Disciplina disciplina = disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada!"));

        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(dados.titulo());
        novaTarefa.setPrazo(dados.prazo());
        novaTarefa.setStatus(StatusTarefa.PENDENTE);
        
        // Vínculo
        novaTarefa.setDisciplina(disciplina);

        // Salvamos DIRETAMENTE a tarefa. 
        // Não precisamos salvar a disciplina inteira se salvarmos a tarefa filha apontando pro pai certo.
        Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);

        return new TarefaResponseDTO(tarefaSalva);
    }

    // Removemos o argumento 'idDisciplina'
    public TarefaResponseDTO atualizarStatus(Long idTarefa, StatusTarefa novoStatus) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));

        // Não precisamos mais validar o pai aqui, pois estamos acessando direto pelo ID único da tarefa.
        
        tarefa.setStatus(novoStatus);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        return new TarefaResponseDTO(tarefaSalva);
    }

    public void deletar(Long idTarefa) {
        if (!tarefaRepository.existsById(idTarefa)) {
             throw new RuntimeException("Tarefa não encontrada!");
        }
        tarefaRepository.deleteById(idTarefa);
    }
}