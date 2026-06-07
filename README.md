# Financas Coletivas

Projeto academico de Orientacao a Objetos.

Tema escolhido: 10 - Aplicativo de Financas Coletivas para Republicas ou Grupos de Viagem.

O sistema permite criar um grupo, adicionar membros, registrar despesas divididas entre todos, listar dividas e registrar pagamentos para quitar essas dividas.

## Tecnologias

- Java 17
- Maven
- JUnit 5
- GitHub Actions

## Estrutura

```text
src/domain           Regras de negocio, entidades e value objects
src/application      Casos de uso
src/infrastructure   Repositorio em memoria
src/presentation     Main para testar o sistema pelo terminal
tests                Testes unitarios
```

## Principais classes

- Aggregate Root: `Grupo`
- Entidades: `Individuo`, `Despesa`, `Divida`, `Pagamento`
- Value Objects: `Dinheiro`, `Dividendo`
- Enum: `MetodoPagamento`

## Como rodar os testes

No Windows, usando o Maven Wrapper:

```bash
.\mvnw.cmd clean test
```

Ou, se o Maven estiver instalado:

```bash
mvn clean test
```

## Como executar o programa

Compile o projeto:

```bash
mvn compile
```

Execute o Main:

```bash
java -cp target/classes presentation.Main
```

No Windows, tambem pode compilar com:

```bash
.\mvnw.cmd compile
```

Depois execute o mesmo comando:

```bash
java -cp target/classes presentation.Main
```

## O que da para testar no Main

Ao executar o programa, o usuario pode:

1. criar um grupo;
2. adicionar membros com ID, nome e saldo inicial;
3. registrar despesas com valores informados pelo usuario;
4. listar dividas pendentes;
5. registrar pagamentos entre membros;
6. listar saldos pessoais e saldos dentro do grupo.

## Regras implementadas

- Uma despesa precisa ter valor, descricao e pagador.
- A despesa e dividida igualmente entre os membros do grupo.
- O sistema gera dividas entre beneficiarios e pagador.
- Dividas na mesma direcao sao consolidadas.
- Dividas opostas sao abatidas.
- Pagamentos reduzem ou quitam dividas.
- Valores monetarios sao tratados em centavos pela classe `Dinheiro`.

## GitHub Actions

O arquivo `.github/workflows/ci.yml` executa os testes automaticamente com:

```bash
mvn clean test
```
