package domain.entities;

import domain.valueobjects.Dinheiro;
import domain.valueobjects.Dividendo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Despesa {

    private final int id;
    private final String descricao;
    private final Dinheiro valor;
    private final Individuo pagador;
    private final List<Dividendo> dividendos;

    public Despesa(int id, String descricao, Dinheiro valor, Individuo pagador, List<Dividendo> dividendos) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descricao da despesa nao pode ser vazia.");
        }
        if (valor == null || valor.ehZero()) {
            throw new IllegalArgumentException("O valor da despesa deve ser maior que zero.");
        }
        if (pagador == null) {
            throw new IllegalArgumentException("O pagador da despesa nao pode ser nulo.");
        }
        if (dividendos == null || dividendos.isEmpty()) {
            throw new IllegalArgumentException("A despesa deve possuir ao menos um dividendo.");
        }

        validarSomaDosDividendos(valor, dividendos);

        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.pagador = pagador;
        this.dividendos = new ArrayList<>(dividendos);
    }

    private void validarSomaDosDividendos(Dinheiro valor, List<Dividendo> dividendos) {
        Dinheiro soma = new Dinheiro(0);
        for (Dividendo dividendo : dividendos) {
            soma = soma.somar(dividendo.getValor());
        }
        if (soma.getCentavos() != valor.getCentavos()) {
            throw new IllegalArgumentException("A soma dos dividendos deve ser igual ao valor total da despesa.");
        }
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Dinheiro getValor() {
        return valor;
    }

    public Individuo getPagador() {
        return pagador;
    }

    public List<Dividendo> getDividendos() {
        return Collections.unmodifiableList(dividendos);
    }
}
