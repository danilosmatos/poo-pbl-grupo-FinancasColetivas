package application.usecases;

import domain.entities.Grupo;
import domain.entities.Individuo;
import infrastructure.repositories.GrupoRepository;

public class AdicionarMembroUseCase {

    private final GrupoRepository repository;

    public AdicionarMembroUseCase(GrupoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositorio de grupos nao pode ser nulo.");
        }

        this.repository = repository;
    }

    public void executar(String nomeGrupo, Individuo membro) {
        Grupo grupo = buscarGrupo(nomeGrupo);
        grupo.adicionarMembro(membro);
        repository.salvar(grupo);
    }

    private Grupo buscarGrupo(String nomeGrupo) {
        return repository.buscarPorNome(nomeGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo nao encontrado."));
    }
}
