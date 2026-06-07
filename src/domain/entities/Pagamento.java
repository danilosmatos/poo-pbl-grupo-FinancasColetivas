package domain.entities;

import domain.valueobjects.Dinheiro;
import domain.valueobjects.MetodoPagamento;

public class Pagamento {

    private final Individuo devedor;
    private final Individuo credor;
    private final Dinheiro valor;
    private final MetodoPagamento metodo;

    public Pagamento(Individuo devedor, Individuo credor, Dinheiro valor, MetodoPagamento metodo) {
        if (devedor == null || credor == null) {
            throw new IllegalArgumentException("Devedor e credor nao podem ser nulos.");
        }
        if (valor == null || valor.ehZero()) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (metodo == null) {
            throw new IllegalArgumentException("O metodo de pagamento nao pode ser nulo.");
        }

        this.devedor = devedor;
        this.credor = credor;
        this.valor = valor;
        this.metodo = metodo;
    }

    public Individuo getDevedor() {
        return devedor;
    }

    public Individuo getCredor() {
        return credor;
    }

    public Dinheiro getValor() {
        return valor;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }
}
