package application.usecases;

import domain.entities.Grupo;
import infrastructure.repositories.GrupoRepository;

public class CriarGrupoUseCase {

    private final GrupoRepository repository;

    public CriarGrupoUseCase(GrupoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositorio de grupos nao pode ser nulo.");
        }

        this.repository = repository;
    }

    public Grupo executar(String nome) {
        Grupo grupo = new Grupo(nome);
        repository.salvar(grupo);
        return grupo;
    }
}
