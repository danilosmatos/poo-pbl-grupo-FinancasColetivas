package domain.entities;

import domain.valueobjects.Dinheiro;
import domain.valueobjects.Dividendo;
import domain.valueobjects.MetodoPagamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grupo {
    private final String nome;
    private boolean aberto;
    private final ArrayList<Individuo> membros;
    private final ArrayList<Despesa> despesas;
    private final ArrayList<Divida> dividas;
    private final ArrayList<Pagamento> pagamentos;
    private int proximoIdDespesa;

    public Grupo(String nome) {
        this.nome = nome;
        this.aberto = true;
        this.membros = new ArrayList<>();
        this.despesas = new ArrayList<>();
        this.dividas = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
        this.proximoIdDespesa = 1;
    }

    public String getNome() {
        return nome;
    }

    public boolean isAberto() {
        return aberto;
    }

    public List<Individuo> getMembros() {
        return Collections.unmodifiableList(membros);
    }

    public List<Despesa> getDespesas() {
        return Collections.unmodifiableList(despesas);
    }

    public List<Divida> getDividas() {
        return Collections.unmodifiableList(dividas);
    }

    public List<Pagamento> getPagamentos() {
        return Collections.unmodifiableList(pagamentos);
    }

    public void abrirGrupo() {
        this.aberto = true;
    }

    public void fecharGrupo() {
        this.aberto = false;
    }

    public void adicionarMembro(Individuo pessoa) {
        if (!this.aberto) {
            System.out.println("Erro: Grupo fechado.");
            return;
        }
        membros.add(pessoa);
    }

    public void removerMembro(int idBusca) {
        if (!this.aberto) {
            System.out.println("Erro: Grupo fechado.");
            return;
        }

        Individuo pessoaParaRemover = null;
        for (Individuo membro : membros) {
            if (membro.getId() == idBusca) {
                pessoaParaRemover = membro;
                break;
            }
        }

        if (pessoaParaRemover != null) {
            membros.remove(pessoaParaRemover);
        } else {
            System.out.println("Membro não encontrado.");
        }
    }

    public void listarMembros() {
        for (Individuo membro : membros) {
            membro.exibirInfo();
        }
    }

    public Despesa registrarDespesa(String descricao, Dinheiro valor, Individuo pagador) {
        validarGrupoAberto();
        validarMembro(pagador);

        if (membros.size() < 2) {
            throw new IllegalStateException("O grupo precisa de ao menos dois membros para registrar despesas.");
        }

        List<Dividendo> dividendos = rateioIgual(valor, membros);
        return registrarDespesaComDividendos(descricao, valor, pagador, dividendos);
    }

    public Despesa registrarDespesaComDividendos(
            String descricao,
            Dinheiro valor,
            Individuo pagador,
            List<Dividendo> dividendos) {
        validarGrupoAberto();
        validarMembro(pagador);
        validarDividendosDosMembros(dividendos);

        Despesa despesa = new Despesa(proximoIdDespesa++, descricao, valor, pagador, dividendos);
        despesas.add(despesa);
        gerarDividas(despesa);
        return despesa;
    }

    public Pagamento registrarPagamento(
            Individuo devedor,
            Individuo credor,
            Dinheiro valor,
            MetodoPagamento metodo) {
        validarGrupoAberto();
        validarMembro(devedor);
        validarMembro(credor);

        Divida divida = buscarDivida(devedor, credor);
        if (divida == null) {
            throw new IllegalStateException("Nao existe divida pendente entre os membros informados.");
        }

        divida.abater(valor);

        if (divida.estaQuitada()) {
            dividas.remove(divida);
        }

        devedor.debitar(valor);
        credor.creditar(valor);

        Pagamento pagamento = new Pagamento(devedor, credor, valor, metodo);
        pagamentos.add(pagamento);
        return pagamento;
    }

    public long obterSaldoLiquidoCentavos(Individuo membro) {
        validarMembro(membro);

        long aReceber = 0;
        long aPagar = 0;

        for (Divida divida : dividas) {
            if (divida.getCredor().getId() == membro.getId()) {
                aReceber += divida.getValorPendente().getCentavos();
            }
            if (divida.getDevedor().getId() == membro.getId()) {
                aPagar += divida.getValorPendente().getCentavos();
            }
        }

        return aReceber - aPagar;
    }

    public void informarSaldo(Individuo membro) {
        validarMembro(membro);

        long saldoLiquido = obterSaldoLiquidoCentavos(membro);
        double valorEmReais = Math.abs(saldoLiquido) / 100.0;

        if (saldoLiquido > 0) {
            System.out.println(membro.getNome() + " tem saldo liquido positivo: a receber R$" + valorEmReais);
        } else if (saldoLiquido < 0) {
            System.out.println(membro.getNome() + " tem saldo liquido negativo: a pagar R$" + valorEmReais);
        } else {
            System.out.println(membro.getNome() + " esta quitado no grupo (saldo liquido zero).");
        }

        double saldoPessoal = membro.getSaldo().getCentavos() / 100.0;
        System.out.println("Saldo pessoal: R$" + saldoPessoal);
    }

    public void informarDebitos() {
        if (dividas.isEmpty()) {
            System.out.println("Nao ha debitos pendentes no grupo.");
            return;
        }

        System.out.println("Debitos pendentes no grupo " + nome + ":");
        for (Divida divida : dividas) {
            double valorEmReais = divida.getValorPendente().getCentavos() / 100.0;
            System.out.println(
                    divida.getDevedor().getNome()
                            + " deve R$"
                            + valorEmReais
                            + " para "
                            + divida.getCredor().getNome()
                            + (divida.getDespesaId() > 0 ? " (despesa #" + divida.getDespesaId() + ")" : ""));
        }
    }

    public void informarDividendos(Despesa despesa) {
        if (!despesas.contains(despesa)) {
            throw new IllegalArgumentException("A despesa informada nao pertence a este grupo.");
        }

        System.out.println("Dividendos da despesa #" + despesa.getId() + " - " + despesa.getDescricao() + ":");
        for (Dividendo dividendo : despesa.getDividendos()) {
            double valorEmReais = dividendo.getValor().getCentavos() / 100.0;
            System.out.println("  " + dividendo.getBeneficiario().getNome() + ": R$" + valorEmReais);
        }
    }

    private void validarGrupoAberto() {
        if (!aberto) {
            throw new IllegalStateException("Operacao financeira nao permitida: grupo fechado.");
        }
    }

    private void validarMembro(Individuo membro) {
        if (membro == null || !membros.contains(membro)) {
            throw new IllegalArgumentException("O individuo informado nao pertence ao grupo.");
        }
    }

    private void validarDividendosDosMembros(List<Dividendo> dividendos) {
        for (Dividendo dividendo : dividendos) {
            if (!membros.contains(dividendo.getBeneficiario())) {
                throw new IllegalArgumentException("Todos os beneficiarios devem ser membros do grupo.");
            }
        }
    }

    private List<Dividendo> rateioIgual(Dinheiro total, List<Individuo> participantes) {
        int quantidade = participantes.size();
        long base = total.getCentavos() / quantidade;
        long resto = total.getCentavos() % quantidade;

        List<Dividendo> resultado = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            long parcela = base + (i < resto ? 1 : 0);
            resultado.add(new Dividendo(participantes.get(i), new Dinheiro(parcela)));
        }
        return resultado;
    }

    private void gerarDividas(Despesa despesa) {
        Individuo pagador = despesa.getPagador();

        for (Dividendo dividendo : despesa.getDividendos()) {
            Individuo beneficiario = dividendo.getBeneficiario();
            if (beneficiario.getId() == pagador.getId()) {
                continue;
            }

            consolidarOuCriarDivida(beneficiario, pagador, dividendo.getValor(), despesa.getId());
        }
    }

    private void consolidarOuCriarDivida(Individuo devedor, Individuo credor, Dinheiro valor, int despesaId) {
        Divida dividaDireta = buscarDivida(devedor, credor);
        if (dividaDireta != null) {
            adicionarValorDivida(dividaDireta, valor);
            return;
        }

        Divida dividaInversa = buscarDivida(credor, devedor);
        if (dividaInversa != null) {
            if (dividaInversa.getValorPendente().maiorOuIgual(valor)) {
                dividaInversa.abater(valor);
                if (dividaInversa.estaQuitada()) {
                    dividas.remove(dividaInversa);
                }
            } else {
                Dinheiro diferenca = valor.subtrair(dividaInversa.getValorPendente());
                dividas.remove(dividaInversa);
                dividas.add(new Divida(devedor, credor, diferenca, despesaId));
            }
            return;
        }

        dividas.add(new Divida(devedor, credor, valor, despesaId));
    }

    private void adicionarValorDivida(Divida divida, Dinheiro valor) {
        Dinheiro novoValor = divida.getValorPendente().somar(valor);
        dividas.remove(divida);
        dividas.add(new Divida(divida.getDevedor(), divida.getCredor(), novoValor, divida.getDespesaId()));
    }

    private Divida buscarDivida(Individuo devedor, Individuo credor) {
        for (Divida divida : dividas) {
            if (divida.getDevedor().getId() == devedor.getId()
                    && divida.getCredor().getId() == credor.getId()) {
                return divida;
            }
        }
        return null;
    }
}
