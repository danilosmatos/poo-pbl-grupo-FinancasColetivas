package domain.valueobjects;

import domain.entities.Individuo;

public final class Dividendo {

    private final Individuo beneficiario;
    private final Dinheiro valor;

    public Dividendo(Individuo beneficiario, Dinheiro valor) {
        if (beneficiario == null) {
            throw new IllegalArgumentException("O beneficiario do dividendo nao pode ser nulo.");
        }
        if (valor == null) {
            throw new IllegalArgumentException("O valor do dividendo nao pode ser nulo.");
        }

        this.beneficiario = beneficiario;
        this.valor = valor;
    }

    public Individuo getBeneficiario() {
        return beneficiario;
    }

    public Dinheiro getValor() {
        return valor;
    }
}
