package infrastructure.repositories;

import domain.entities.Grupo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GrupoRepositoryEmMemoria implements GrupoRepository {

    private final Map<String, Grupo> gruposPorNome = new HashMap<>();

    @Override
    public void salvar(Grupo grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("O grupo nao pode ser nulo.");
        }

        gruposPorNome.put(grupo.getNome(), grupo);
    }

    @Override
    public Optional<Grupo> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do grupo nao pode ser vazio.");
        }

        return Optional.ofNullable(gruposPorNome.get(nome));
    }

    @Override
    public List<Grupo> listarTodos() {
        return new ArrayList<>(gruposPorNome.values());
    }
}
