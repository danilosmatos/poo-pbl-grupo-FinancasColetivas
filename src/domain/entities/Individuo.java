package domain.entities;

import domain.valueobjects.Dinheiro;

public class Individuo {
    private final int id;
    private final String nome;
    private Dinheiro saldo;

    public Individuo(int id, String nome, Dinheiro saldo){
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Dinheiro getSaldo() {
        return saldo;
    }

    public void exibirInfo() {

        double valorEmReais = saldo.getCentavos() / 100.0;
        System.out.println("Nome:" + nome + " | ID:" + id + " | Saldo: R$" + valorEmReais);
    }
}
