package application.usecases;

import domain.entities.Despesa;
import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.valueobjects.Dinheiro;
import infrastructure.repositories.GrupoRepository;

public class RegistrarDespesaUseCase {

    private final GrupoRepository repository;

    public RegistrarDespesaUseCase(GrupoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositorio de grupos nao pode ser nulo.");
        }

        this.repository = repository;
    }

    public Despesa executar(String nomeGrupo, String descricao, Dinheiro valor, Individuo pagador) {
        Grupo grupo = buscarGrupo(nomeGrupo);
        Despesa despesa = grupo.registrarDespesa(descricao, valor, pagador);
        repository.salvar(grupo);
        return despesa;
    }

    private Grupo buscarGrupo(String nomeGrupo) {
        return repository.buscarPorNome(nomeGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo nao encontrado."));
    }
}
