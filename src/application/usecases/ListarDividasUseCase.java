package application.usecases;

import domain.entities.Divida;
import domain.entities.Grupo;
import infrastructure.repositories.GrupoRepository;

import java.util.List;

public class ListarDividasUseCase {

    private final GrupoRepository repository;

    public ListarDividasUseCase(GrupoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositorio de grupos nao pode ser nulo.");
        }

        this.repository = repository;
    }

    public List<Divida> executar(String nomeGrupo) {
        Grupo grupo = repository.buscarPorNome(nomeGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo nao encontrado."));

        return grupo.getDividas();
    }
}
