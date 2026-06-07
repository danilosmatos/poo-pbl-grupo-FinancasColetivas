package domain.valueobjects;

public final class Dinheiro {

    private final long centavos;

    public Dinheiro(long centavos) {
        if (centavos < 0) {
            throw new IllegalArgumentException("O valor monetario nao pode ser negativo.");
        }

        this.centavos = centavos;
    }

    public long getCentavos() {
        return centavos;
    }

    public Dinheiro somar(Dinheiro outro) {
        return new Dinheiro(this.centavos + outro.centavos);
    }

    public Dinheiro subtrair(Dinheiro outro) {
        long resultado = this.centavos - outro.centavos;
        if (resultado < 0) {
            throw new IllegalArgumentException("O resultado da subtracao nao pode ser negativo.");
        }
        return new Dinheiro(resultado);
    }

    public boolean maiorOuIgual(Dinheiro outro) {
        return this.centavos >= outro.centavos;
    }

    public boolean ehZero() {
        return centavos == 0;
    }
}