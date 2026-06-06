package domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void deveSomarValores() {
        Dinheiro resultado = new Dinheiro(500).somar(new Dinheiro(300));

        assertEquals(800, resultado.getCentavos());
    }

    @Test
    void deveSubtrairValores() {
        Dinheiro resultado = new Dinheiro(500).subtrair(new Dinheiro(200));

        assertEquals(300, resultado.getCentavos());
    }

    @Test
    void naoDeveSubtrairResultadoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Dinheiro(100).subtrair(new Dinheiro(200)));
    }

    @Test
    void deveCompararValores() {
        assertTrue(new Dinheiro(500).maiorOuIgual(new Dinheiro(500)));
        assertTrue(new Dinheiro(600).maiorOuIgual(new Dinheiro(500)));
        assertFalse(new Dinheiro(400).maiorOuIgual(new Dinheiro(500)));
    }

    @Test
    void deveIdentificarValorZero() {
        assertTrue(new Dinheiro(0).ehZero());
        assertFalse(new Dinheiro(1).ehZero());
    }
}
