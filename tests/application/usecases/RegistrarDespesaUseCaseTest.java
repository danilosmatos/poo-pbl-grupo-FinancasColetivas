package application.usecases;

import domain.entities.Despesa;
import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.valueobjects.Dinheiro;
import infrastructure.repositories.GrupoRepositoryEmMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrarDespesaUseCaseTest {

    @Test
    void deveRegistrarDespesaEGerarDividasPeloUseCase() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        Grupo grupo = new Grupo("Viagem");
        Individuo ana = new Individuo(1, "Ana", new Dinheiro(10000));
        Individuo bruno = new Individuo(2, "Bruno", new Dinheiro(5000));
        grupo.adicionarMembro(ana);
        grupo.adicionarMembro(bruno);
        repository.salvar(grupo);
        RegistrarDespesaUseCase useCase = new RegistrarDespesaUseCase(repository);

        Despesa despesa = useCase.executar("Viagem", "Mercado", new Dinheiro(4000), ana);

        assertEquals("Mercado", despesa.getDescricao());
        assertEquals(1, grupo.getDespesas().size());
        assertEquals(1, grupo.getDividas().size());
        assertEquals(2000, grupo.getDividas().get(0).getValorPendente().getCentavos());
    }

    @Test
    void naoDeveRegistrarDespesaEmGrupoInexistente() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        RegistrarDespesaUseCase useCase = new RegistrarDespesaUseCase(repository);
        Individuo ana = new Individuo(1, "Ana", new Dinheiro(10000));

        assertThrows(IllegalArgumentException.class,
                () -> useCase.executar("Inexistente", "Mercado", new Dinheiro(4000), ana));
    }
}
