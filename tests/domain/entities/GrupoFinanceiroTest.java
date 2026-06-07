package domain.entities;

import domain.valueobjects.Dinheiro;
import domain.valueobjects.Dividendo;
import domain.valueobjects.MetodoPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrupoFinanceiroTest {

    private Grupo grupo;
    private Individuo alice;
    private Individuo bob;
    private Individuo carlos;

    @BeforeEach
    void setUp() {
        grupo = new Grupo("Viagem");
        alice = new Individuo(1, "Alice", new Dinheiro(10000));
        bob = new Individuo(2, "Bob", new Dinheiro(5000));
        carlos = new Individuo(3, "Carlos", new Dinheiro(5000));

        grupo.adicionarMembro(alice);
        grupo.adicionarMembro(bob);
        grupo.adicionarMembro(carlos);
    }

    @Test
    void deveRegistrarDespesaComRateioIgual() {
        Despesa despesa = grupo.registrarDespesa("Jantar", new Dinheiro(9000), alice);

        assertEquals(1, despesa.getId());
        assertEquals(3, despesa.getDividendos().size());
        assertEquals(9000, somaDividendos(despesa));
        assertEquals(2, grupo.getDividas().size());
    }

    @Test
    void deveGerarDividasCorretasAposDespesa() {
        grupo.registrarDespesa("Jantar", new Dinheiro(9000), alice);

        assertEquals(3000, buscarDividaPendente(bob, alice));
        assertEquals(3000, buscarDividaPendente(carlos, alice));
        assertEquals(6000, grupo.obterSaldoLiquidoCentavos(alice));
        assertEquals(-3000, grupo.obterSaldoLiquidoCentavos(bob));
        assertEquals(-3000, grupo.obterSaldoLiquidoCentavos(carlos));
    }

    @Test
    void deveRegistrarPagamentoEQuitarDivida() {
        grupo.registrarDespesa("Jantar", new Dinheiro(9000), alice);

        Pagamento pagamento = grupo.registrarPagamento(bob, alice, new Dinheiro(3000), MetodoPagamento.PIX);

        assertEquals(MetodoPagamento.PIX, pagamento.getMetodo());
        assertEquals(1, grupo.getDividas().size());
        assertEquals(0, buscarDividaPendente(bob, alice));
        assertEquals(13000, alice.getSaldo().getCentavos());
        assertEquals(2000, bob.getSaldo().getCentavos());
    }

    @Test
    void deveRegistrarDespesaComRateioCustomizado() {
        List<Dividendo> dividendos = List.of(
                new Dividendo(alice, new Dinheiro(2000)),
                new Dividendo(bob, new Dinheiro(3000)),
                new Dividendo(carlos, new Dinheiro(5000)));

        Despesa despesa = grupo.registrarDespesaComDividendos("Aluguel", new Dinheiro(10000), bob, dividendos);

        assertEquals(10000, somaDividendos(despesa));
        assertEquals(2000, buscarDividaPendente(alice, bob));
        assertEquals(5000, buscarDividaPendente(carlos, bob));
    }

    @Test
    void naoDeveRegistrarDespesaComGrupoFechado() {
        grupo.fecharGrupo();

        assertThrows(IllegalStateException.class,
                () -> grupo.registrarDespesa("Jantar", new Dinheiro(3000), alice));
    }

    @Test
    void naoDeveRegistrarPagamentoSemDivida() {
        assertThrows(IllegalStateException.class,
                () -> grupo.registrarPagamento(bob, alice, new Dinheiro(1000), MetodoPagamento.DINHEIRO));
    }

    @Test
    void naoDeveRegistrarPagamentoMaiorQueDivida() {
        grupo.registrarDespesa("Cafe", new Dinheiro(3000), alice);

        assertThrows(IllegalArgumentException.class,
                () -> grupo.registrarPagamento(bob, alice, new Dinheiro(5000), MetodoPagamento.PIX));
    }

    @Test
    void deveConsolidarDividasNaMesmaDirecao() {
        grupo.registrarDespesa("Almoco", new Dinheiro(3000), alice);
        grupo.registrarDespesa("Cafe", new Dinheiro(3000), alice);

        assertEquals(2, grupo.getDividas().size());
        assertEquals(2000, buscarDividaPendente(bob, alice));
        assertEquals(2000, buscarDividaPendente(carlos, alice));
    }

    @Test
    void deveNetearDividasEmDirecoesOpostas() {
        grupo.registrarDespesa("Item 1", new Dinheiro(9000), alice);
        grupo.registrarDespesa("Item 2", new Dinheiro(3000), bob);

        assertEquals(2000, buscarDividaPendente(bob, alice));
        assertEquals(3000, buscarDividaPendente(carlos, alice));
    }

    @Test
    void deveManterSaldoLiquidoZeroAposQuitarTodasDividas() {
        grupo.registrarDespesa("Jantar", new Dinheiro(9000), alice);

        grupo.registrarPagamento(bob, alice, new Dinheiro(3000), MetodoPagamento.PIX);
        grupo.registrarPagamento(carlos, alice, new Dinheiro(3000), MetodoPagamento.TRANSFERENCIA);

        assertTrue(grupo.getDividas().isEmpty());
        assertEquals(0, grupo.obterSaldoLiquidoCentavos(alice));
        assertEquals(0, grupo.obterSaldoLiquidoCentavos(bob));
        assertEquals(0, grupo.obterSaldoLiquidoCentavos(carlos));
    }

    @Test
    void deveManterDividasSeparadasParaDevedoresDiferentes() {
        grupo.registrarDespesa("Mercado", new Dinheiro(9000), alice);

        assertEquals(2, grupo.getDividas().size());
        assertEquals(3000, buscarDividaPendente(bob, alice));
        assertEquals(3000, buscarDividaPendente(carlos, alice));
    }

    @Test
    void deveCreditarCredorEDebitarDevedorAoRegistrarPagamento() {
        grupo.registrarDespesa("Jantar", new Dinheiro(9000), alice);

        grupo.registrarPagamento(bob, alice, new Dinheiro(3000), MetodoPagamento.PIX);

        assertEquals(13000, alice.getSaldo().getCentavos());
        assertEquals(2000, bob.getSaldo().getCentavos());
    }

    @Test
    void naoDeveRegistrarDespesaComPagadorForaDoGrupo() {
        Individuo externo = new Individuo(99, "Externo", new Dinheiro(1000));

        assertThrows(IllegalArgumentException.class,
                () -> grupo.registrarDespesa("Compra externa", new Dinheiro(3000), externo));
    }

    @Test
    void naoDeveRegistrarDespesaComBeneficiarioForaDoGrupo() {
        Individuo externo = new Individuo(99, "Externo", new Dinheiro(1000));
        List<Dividendo> dividendos = List.of(
                new Dividendo(alice, new Dinheiro(2000)),
                new Dividendo(externo, new Dinheiro(1000)));

        assertThrows(IllegalArgumentException.class,
                () -> grupo.registrarDespesaComDividendos("Compra invalida", new Dinheiro(3000), alice, dividendos));
    }

    @Test
    void naoDeveRegistrarDespesaComSomaDeDividendosDiferenteDoTotal() {
        List<Dividendo> dividendos = List.of(
                new Dividendo(alice, new Dinheiro(1000)),
                new Dividendo(bob, new Dinheiro(1000)));

        assertThrows(IllegalArgumentException.class,
                () -> grupo.registrarDespesaComDividendos("Conta invalida", new Dinheiro(3000), alice, dividendos));
    }

    @Test
    void naoDeveRegistrarPagamentoComGrupoFechado() {
        grupo.registrarDespesa("Jantar", new Dinheiro(9000), alice);
        grupo.fecharGrupo();

        assertThrows(IllegalStateException.class,
                () -> grupo.registrarPagamento(bob, alice, new Dinheiro(3000), MetodoPagamento.PIX));
    }

    private long somaDividendos(Despesa despesa) {
        long soma = 0;
        for (var dividendo : despesa.getDividendos()) {
            soma += dividendo.getValor().getCentavos();
        }
        return soma;
    }

    private long buscarDividaPendente(Individuo devedor, Individuo credor) {
        return grupo.getDividas().stream()
                .filter(d -> d.getDevedor().getId() == devedor.getId()
                        && d.getCredor().getId() == credor.getId())
                .mapToLong(d -> d.getValorPendente().getCentavos())
                .findFirst()
                .orElse(0L);
    }
}
