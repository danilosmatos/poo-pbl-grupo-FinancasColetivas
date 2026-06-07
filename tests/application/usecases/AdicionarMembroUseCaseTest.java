package application.usecases;

import domain.entities.Grupo;
import domain.entities.Individuo;
import domain.valueobjects.Dinheiro;
import infrastructure.repositories.GrupoRepositoryEmMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdicionarMembroUseCaseTest {

    @Test
    void deveAdicionarMembroEmGrupoExistente() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        Grupo grupo = new Grupo("Republica");
        repository.salvar(grupo);
        AdicionarMembroUseCase useCase = new AdicionarMembroUseCase(repository);

        useCase.executar("Republica", new Individuo(1, "Ana", new Dinheiro(1000)));

        assertEquals(1, grupo.getMembros().size());
    }

    @Test
    void naoDeveAdicionarMembroEmGrupoInexistente() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        AdicionarMembroUseCase useCase = new AdicionarMembroUseCase(repository);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.executar("Inexistente", new Individuo(1, "Ana", new Dinheiro(1000))));
    }
}
