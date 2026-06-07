package domain.entities;

import domain.valueobjects.Dinheiro;

public class Divida {

    private final Individuo devedor;
    private final Individuo credor;
    private Dinheiro valorPendente;
    private final int despesaId;

    public Divida(Individuo devedor, Individuo credor, Dinheiro valor, int despesaId) {
        if (devedor == null || credor == null) {
            throw new IllegalArgumentException("Devedor e credor nao podem ser nulos.");
        }
        if (devedor.getId() == credor.getId()) {
            throw new IllegalArgumentException("Devedor e credor devem ser pessoas diferentes.");
        }
        if (valor == null || valor.ehZero()) {
            throw new IllegalArgumentException("O valor da divida deve ser maior que zero.");
        }

        this.devedor = devedor;
        this.credor = credor;
        this.valorPendente = valor;
        this.despesaId = despesaId;
    }

    public Individuo getDevedor() {
        return devedor;
    }

    public Individuo getCredor() {
        return credor;
    }

    public Dinheiro getValorPendente() {
        return valorPendente;
    }

    public int getDespesaId() {
        return despesaId;
    }

    public boolean estaQuitada() {
        return valorPendente.ehZero();
    }

    public void abater(Dinheiro pagamento) {
        if (pagamento == null || pagamento.ehZero()) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (!valorPendente.maiorOuIgual(pagamento)) {
            throw new IllegalArgumentException("O pagamento excede o valor pendente da divida.");
        }
        valorPendente = valorPendente.subtrair(pagamento);
    }
}
