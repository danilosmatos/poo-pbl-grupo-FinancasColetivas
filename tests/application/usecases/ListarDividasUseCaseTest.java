package application.usecases;

import domain.entities.Divida;
import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.valueobjects.Dinheiro;
import infrastructure.repositories.GrupoRepositoryEmMemoria;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListarDividasUseCaseTest {

    @Test
    void deveListarDividasDoGrupo() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        Grupo grupo = new Grupo("Viagem");
        Individuo ana = new Individuo(1, "Ana", new Dinheiro(10000));
        Individuo bruno = new Individuo(2, "Bruno", new Dinheiro(5000));
        grupo.adicionarMembro(ana);
        grupo.adicionarMembro(bruno);
        grupo.registrarDespesa("Mercado", new Dinheiro(4000), ana);
        repository.salvar(grupo);
        ListarDividasUseCase useCase = new ListarDividasUseCase(repository);

        List<Divida> dividas = useCase.executar("Viagem");

        assertEquals(1, dividas.size());
        assertEquals(2000, dividas.get(0).getValorPendente().getCentavos());
    }

    @Test
    void naoDeveListarDividasDeGrupoInexistente() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        ListarDividasUseCase useCase = new ListarDividasUseCase(repository);

        assertThrows(IllegalArgumentException.class, () -> useCase.executar("Inexistente"));
    }
}
