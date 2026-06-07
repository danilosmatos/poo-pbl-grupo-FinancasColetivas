package application.usecases;

import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.entities.Pagamento;
import domain.valueobjects.Dinheiro;
import domain.valueobjects.MetodoPagamento;
import infrastructure.repositories.GrupoRepository;

public class RegistrarPagamentoUseCase {

    private final GrupoRepository repository;

    public RegistrarPagamentoUseCase(GrupoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositorio de grupos nao pode ser nulo.");
        }

        this.repository = repository;
    }

    public Pagamento executar(
            String nomeGrupo,
            Individuo devedor,
            Individuo credor,
            Dinheiro valor,
            MetodoPagamento metodo) {
        Grupo grupo = buscarGrupo(nomeGrupo);
        Pagamento pagamento = grupo.registrarPagamento(devedor, credor, valor, metodo);
        repository.salvar(grupo);
        return pagamento;
    }

    private Grupo buscarGrupo(String nomeGrupo) {
        return repository.buscarPorNome(nomeGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo nao encontrado."));
    }
}
