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
}