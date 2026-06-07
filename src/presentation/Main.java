package presentation;

import application.usecases.AdicionarMembroUseCase;
import application.usecases.CriarGrupoUseCase;
import application.usecases.ListarDividasUseCase;
import application.usecases.RegistrarDespesaUseCase;
import application.usecases.RegistrarPagamentoUseCase;
import domain.entities.Divida;
import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.valueobjects.Dinheiro;
import domain.valueobjects.MetodoPagamento;
import infrastructure.repositories.GrupoRepository;
import infrastructure.repositories.GrupoRepositoryEmMemoria;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final GrupoRepository repository = new GrupoRepositoryEmMemoria();

    private final CriarGrupoUseCase criarGrupo = new CriarGrupoUseCase(repository);
    private final AdicionarMembroUseCase adicionarMembro = new AdicionarMembroUseCase(repository);
    private final RegistrarDespesaUseCase registrarDespesa = new RegistrarDespesaUseCase(repository);
    private final RegistrarPagamentoUseCase registrarPagamento = new RegistrarPagamentoUseCase(repository);
    private final ListarDividasUseCase listarDividas = new ListarDividasUseCase(repository);

    private Grupo grupo;

    public static void main(String[] args) {
        new Main().executar();
    }

    private void executar() {
        System.out.println("=== Financas Coletivas ===");
        criarGrupoInicial();

        String opcao;
        do {
            exibirMenu();
            opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> adicionarMembro();
                    case "2" -> listarMembros();
                    case "3" -> registrarDespesa();
                    case "4" -> listarDividas();
                    case "5" -> registrarPagamento();
                    case "6" -> System.out.println("Encerrando o programa.");
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (RuntimeException erro) {
                System.out.println("Erro: " + erro.getMessage());
            }

            System.out.println();
        } while (!"6".equals(opcao));
    }

    private void criarGrupoInicial() {
        String nome = lerTextoObrigatorio("Nome do grupo: ");
        grupo = criarGrupo.executar(nome);
        System.out.println("Grupo criado: " + grupo.getNome());
        System.out.println();
    }

    private void exibirMenu() {
        System.out.println("Escolha uma opcao:");
        System.out.println("1 - Adicionar membro");
        System.out.println("2 - Listar membros e saldos");
        System.out.println("3 - Registrar despesa dividida entre todos");
        System.out.println("4 - Listar dividas pendentes");
        System.out.println("5 - Registrar pagamento de divida");
        System.out.println("6 - Sair");
        System.out.print("Opcao: ");
    }

    private void adicionarMembro() {
        int id = lerInteiro("ID do membro: ");

        if (buscarMembroPorId(id) != null) {
            System.out.println("Ja existe um membro com esse ID.");
            return;
        }

        String nome = lerTextoObrigatorio("Nome do membro: ");
        Dinheiro saldo = lerDinheiro("Saldo inicial do membro: R$ ");

        Individuo membro = new Individuo(id, nome, saldo);
        adicionarMembro.executar(grupo.getNome(), membro);

        System.out.println("Membro adicionado.");
    }

    private void listarMembros() {
        if (grupo.getMembros().isEmpty()) {
            System.out.println("Nao ha membros cadastrados.");
            return;
        }

        System.out.println("Membros do grupo:");
        for (Individuo membro : grupo.getMembros()) {
            long saldoLiquido = grupo.obterSaldoLiquidoCentavos(membro);
            System.out.println("- ID " + membro.getId()
                    + " | " + membro.getNome()
                    + " | saldo pessoal: " + formatar(membro.getSaldo())
                    + " | saldo no grupo: " + formatarSaldoLiquido(saldoLiquido));
        }
    }

    private void registrarDespesa() {
        if (grupo.getMembros().size() < 2) {
            System.out.println("Cadastre pelo menos dois membros antes de registrar despesas.");
            return;
        }

        String descricao = lerTextoObrigatorio("Descricao da despesa: ");
        Dinheiro valor = lerDinheiro("Valor total da despesa: R$ ");
        int idPagador = lerInteiro("ID de quem pagou: ");

        Individuo pagador = buscarMembroPorId(idPagador);
        if (pagador == null) {
            System.out.println("Pagador nao encontrado.");
            return;
        }

        registrarDespesa.executar(grupo.getNome(), descricao, valor, pagador);
        System.out.println("Despesa registrada e dividida igualmente entre os membros.");
    }

    private void listarDividas() {
        List<Divida> dividas = listarDividas.executar(grupo.getNome());

        if (dividas.isEmpty()) {
            System.out.println("Nao ha dividas pendentes.");
            return;
        }

        System.out.println("Dividas pendentes:");
        for (Divida divida : dividas) {
            System.out.println("- " + divida.getDevedor().getNome()
                    + " deve " + formatar(divida.getValorPendente())
                    + " para " + divida.getCredor().getNome());
        }
    }

    private void registrarPagamento() {
        if (listarDividas.executar(grupo.getNome()).isEmpty()) {
            System.out.println("Nao ha dividas para pagar.");
            return;
        }

        listarDividas();

        int idDevedor = lerInteiro("ID de quem vai pagar: ");
        int idCredor = lerInteiro("ID de quem vai receber: ");
        Dinheiro valor = lerDinheiro("Valor do pagamento: R$ ");
        MetodoPagamento metodo = lerMetodoPagamento();

        Individuo devedor = buscarMembroPorId(idDevedor);
        Individuo credor = buscarMembroPorId(idCredor);

        if (devedor == null || credor == null) {
            System.out.println("Devedor ou credor nao encontrado.");
            return;
        }

        registrarPagamento.executar(grupo.getNome(), devedor, credor, valor, metodo);
        System.out.println("Pagamento registrado.");
    }

    private MetodoPagamento lerMetodoPagamento() {
        System.out.println("Metodo de pagamento:");
        MetodoPagamento[] metodos = MetodoPagamento.values();
        for (int i = 0; i < metodos.length; i++) {
            System.out.println((i + 1) + " - " + metodos[i]);
        }

        int opcao = lerInteiro("Opcao: ");
        if (opcao < 1 || opcao > metodos.length) {
            throw new IllegalArgumentException("Metodo de pagamento invalido.");
        }

        return metodos[opcao - 1];
    }

    private Individuo buscarMembroPorId(int id) {
        for (Individuo membro : grupo.getMembros()) {
            if (membro.getId() == id) {
                return membro;
            }
        }
        return null;
    }

    private String lerTextoObrigatorio(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();

        if (texto.isBlank()) {
            throw new IllegalArgumentException("O texto informado nao pode ser vazio.");
        }

        return texto;
    }

    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException("Informe um numero inteiro valido.");
        }
    }

    private Dinheiro lerDinheiro(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine()
                .trim()
                .replace("R$", "")
                .replace(" ", "")
                .replace(",", ".");

        try {
            BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
            long centavos = valor.multiply(new BigDecimal("100")).longValueExact();
            return new Dinheiro(centavos);
        } catch (ArithmeticException | NumberFormatException erro) {
            throw new IllegalArgumentException("Informe um valor monetario valido. Exemplo: 25,50");
        }
    }

    private String formatar(Dinheiro dinheiro) {
        long centavos = dinheiro.getCentavos();
        return "R$ " + (centavos / 100) + "," + String.format("%02d", Math.abs(centavos % 100));
    }

    private String formatarSaldoLiquido(long centavos) {
        String sinal;
        if (centavos > 0) {
            sinal = "a receber ";
        } else if (centavos < 0) {
            sinal = "a pagar ";
        } else {
            return "quitado";
        }

        long absoluto = Math.abs(centavos);
        return sinal + "R$ " + (absoluto / 100) + "," + String.format("%02d", absoluto % 100);
    }
}
