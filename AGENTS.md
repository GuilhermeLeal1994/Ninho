# Projeto: Ninho — Sistema de Telemetria de Sensores

## Contexto

O Ninho é um projeto acadêmico desenvolvido para a disciplina de Programação Orientada a Objetos.

O projeto utiliza como base a proposta "Sistema de Telemetria de Sensores".

O sistema tem como objetivo monitorar condições internas de uma residência por meio de telemetria, utilizando três tipos de leitura:

- Temperatura
- Umidade
- Luminosidade

O desenvolvimento será realizado de forma incremental durante o bimestre, seguindo as etapas definidas pela disciplina.

## Tecnologias

- Java 25
- Maven
- JUnit 5
- JDBC
- SQLite
- Git
- GitHub

## Pacotes

O projeto utiliza a seguinte organização:

### `domain`

Contém as entidades e regras de negócio.

Classes previstas:

- `Sensor`
- `LeituraTelemetria`
- `Alerta`
- `AnalisadorDeAnomalias`

### `infra`

Contém componentes relacionados à infraestrutura.

Classes previstas:

- `FilaDeLeituras`
- `AlertaRepository`
- `LogArquivo`

### `app`

Responsável pela inicialização e integração dos componentes.

Classe prevista:

- `Main`

### Testes

Os testes ficam em:

`src/test/java`

e devem acompanhar a estrutura de pacotes do código principal.

## Regras de arquitetura

- O domínio não deve depender da infraestrutura.
- Classes do pacote `domain` não devem acessar banco de dados.
- Classes do pacote `domain` não devem realizar operações de arquivo.
- O domínio não deve importar `java.sql`.
- O domínio não deve conhecer detalhes de JDBC.
- Regras de negócio devem permanecer independentes de threads e banco de dados sempre que possível.
- `AnalisadorDeAnomalias` deve ser testável sem banco de dados e sem threads.
- `Sensor` deve ser responsável apenas pela geração e publicação de leituras.
- `Sensor` não deve conhecer banco de dados ou sistema de logs.
- O acesso ao banco deve ser isolado por Repository/DAO.
- O registro de leituras em arquivo deve permanecer na infraestrutura.
- Cada classe deve possuir uma responsabilidade clara.

## Regras de anomalia atuais

As regras iniciais definidas para o Ninho são:

- Temperatura acima de 30 °C: anomalia.
- Umidade abaixo de 30%: anomalia.
- Luminosidade abaixo de 100 lux: anomalia.

Esses valores fazem parte da implementação inicial e poderão ser ajustados conforme a evolução do projeto.

## Concorrência

A arquitetura está sendo preparada para o modelo produtor-consumidor previsto na especificação.

Quando a etapa de concorrência for implementada:

- `Sensor` deverá implementar `Runnable`.
- Cada sensor simulado deverá executar em sua própria thread.
- Os sensores publicarão leituras em uma fila compartilhada.
- A fila deverá ser thread-safe.
- A implementação deverá evitar perda e duplicação de leituras.
- A implementação deverá evitar race conditions e deadlocks.

A fila compartilhada será o principal estado compartilhado entre as threads.

Não antecipar a implementação dessas funcionalidades antes da etapa correspondente do projeto.

## Persistência

A persistência será implementada posteriormente utilizando JDBC e SQLite.

Quando essa etapa for desenvolvida:

- Alertas deverão ser persistidos no banco de dados.
- O SQL deverá permanecer isolado no Repository/DAO.
- Classes de domínio não deverão acessar JDBC diretamente.
- O banco de dados não deverá fazer parte dos testes unitários das regras de negócio.

## Logs

O sistema deverá registrar as leituras em arquivo quando a etapa de I/O for implementada.

A responsabilidade de escrita em arquivo ficará na infraestrutura e não no domínio.

## TDD e testes

Novas regras de negócio devem ser desenvolvidas utilizando testes.

Antes de considerar uma alteração concluída, executar:

```bash
mvn clean test
```

Todos os testes devem permanecer verdes.

Os testes unitários das regras de negócio não devem depender de:

- Threads reais;
- Banco de dados real;
- Rede;
- Arquivos externos.

## Como trabalhar no projeto

Ao realizar alterações:

1. Entender o requisito antes de escrever código.
2. Explicar o plano antes de editar.
3. Trabalhar em uma classe ou responsabilidade por vez.
4. Criar ou atualizar testes para regras de negócio.
5. Executar os testes após cada mudança relevante.
6. Não continuar com testes quebrados.
7. Fazer commits pequenos e com mensagens claras.
8. Não implementar etapas futuras antecipadamente.

## Estilo de código

- Utilizar nomes claros e descritivos.
- Preferir nomes em português.
- Manter consistência entre classes, métodos e variáveis.
- Manter os atributos encapsulados.
- Evitar métodos excessivamente grandes.
- Evitar duplicação de código.
- Priorizar soluções simples.
- Não adicionar dependências sem necessidade.
- O código deve ser compreensível e explicável pelos integrantes do projeto.

## Uso de Inteligência Artificial

Ferramentas de Inteligência Artificial podem ser utilizadas como apoio para planejamento, implementação, testes, revisão e documentação.

Toda sugestão gerada por IA deve ser revisada antes de entrar no projeto.

Regras:

- Não aceitar código sem compreender seu funcionamento.
- Compilar e testar alterações sugeridas por IA.
- Não fornecer senhas, tokens ou chaves de API.
- Não fornecer dados pessoais ou informações sensíveis.
- Utilizar a IA como ferramenta de apoio e aprendizado.

## Estado atual do projeto

### Domínio e regras de anomalia

Implementado:

- `LeituraTelemetria`
- `Alerta`
- `AnalisadorDeAnomalias`
- Testes unitários das regras de anomalia

Resultado atual esperado:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Ainda não implementado:

- Execução concorrente dos sensores;
- Fila compartilhada;
- Persistência JDBC;
- Banco SQLite;
- Log das leituras.

Essas funcionalidades serão desenvolvidas nas etapas posteriores.