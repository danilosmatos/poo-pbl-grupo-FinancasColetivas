package domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DinheiroTest {

    @Test
    void deveCriarDinheiroComValorValido() {
        Dinheiro dinheiro = new Dinheiro(1000);

        assertEquals(1000, dinheiro.getCentavos());
    }

    @Test
    void naoDeveCriarDinheiroComValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Dinheiro(-1));
    }
}