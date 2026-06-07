package application.usecases;

import domain.entities.Grupo;
import infrastructure.repositories.GrupoRepositoryEmMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CriarGrupoUseCaseTest {

    @Test
    void deveCriarESalvarGrupoNoRepositorio() {
        GrupoRepositoryEmMemoria repository = new GrupoRepositoryEmMemoria();
        CriarGrupoUseCase useCase = new CriarGrupoUseCase(repository);

        Grupo grupo = useCase.executar("Viagem");

        assertEquals("Viagem", grupo.getNome());
        assertTrue(repository.buscarPorNome("Viagem").isPresent());
    }
}
