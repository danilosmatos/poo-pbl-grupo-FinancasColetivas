package application.usecases;

import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.entities.Pagamento;
import domain.valueobjects.Dinheiro;
import domain.valueobjects.MetodoPagamento;
import infrastructure.repositories.GrupoRepositoryEmMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrarPagamentoUseCaseTest {

    @Test
    void deveRegistrarPagamentoEReduzirDividaPeloUseCase() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        Grupo grupo = new Grupo("Viagem");
        Individuo ana = new Individuo(1, "Ana", new Dinheiro(10000));
        Individuo bruno = new Individuo(2, "Bruno", new Dinheiro(5000));
        grupo.adicionarMembro(ana);
        grupo.adicionarMembro(bruno);
        grupo.registrarDespesa("Mercado", new Dinheiro(4000), ana);
        repository.salvar(grupo);
        RegistrarPagamentoUseCase useCase = new RegistrarPagamentoUseCase(repository);

        Pagamento pagamento = useCase.executar("Viagem", bruno, ana, new Dinheiro(2000), MetodoPagamento.PIX);

        assertEquals(MetodoPagamento.PIX, pagamento.getMetodo());
        assertTrue(grupo.getDividas().isEmpty());
        assertEquals(12000, ana.getSaldo().getCentavos());
        assertEquals(3000, bruno.getSaldo().getCentavos());
    }
}
