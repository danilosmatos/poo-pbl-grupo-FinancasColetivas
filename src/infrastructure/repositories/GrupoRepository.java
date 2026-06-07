package infrastructure.repositories;

import domain.entities.Grupo;

import java.util.List;
import java.util.Optional;

public interface GrupoRepository {

    void salvar(Grupo grupo);

    Optional<Grupo> buscarPorNome(String nome);

    List<Grupo> listarTodos();
}
