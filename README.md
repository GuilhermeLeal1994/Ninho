# 🏠 Ninho

**Sistema inteligente de monitoramento de conforto residencial**

O **Ninho** é um projeto desenvolvido para a disciplina de **Programação Orientada a Objetos**, com o objetivo de construir um sistema de recepção e análise de telemetria utilizando **Java**.

O sistema foi pensado para monitorar as condições de ambientes internos de uma residência por meio de dados de:

- 🌡️ Temperatura
- 💧 Umidade
- 💡 Luminosidade

A partir dessas leituras, o sistema identifica condições consideradas anômalas e gera alertas.

O desenvolvimento será realizado de forma incremental, seguindo as etapas semanais propostas na atividade acadêmica.

---

## 🤖 Projeto Ninho

A proposta do Ninho é aplicar os conceitos do trabalho acadêmico em um cenário real de monitoramento residencial.

Além dos sensores simulados previstos na atividade, futuramente o projeto poderá ser integrado a um dispositivo físico equipado com sensores de temperatura, umidade e luminosidade.

O dispositivo também poderá contar com uma matriz de LEDs para representar visualmente informações e estados do ambiente.

---

## 🚧 Status do desenvolvimento

### ✅ Semana 1 — Domínio e regras de anomalia (TDD)

Na primeira etapa foram desenvolvidas as classes responsáveis pela representação das leituras, alertas e análise das condições monitoradas.

Classes implementadas:

- `LeituraTelemetria`
- `Alerta`
- `AnalisadorDeAnomalias`

As regras foram desenvolvidas utilizando **TDD (Test Driven Development)**, criando testes unitários para validar os comportamentos do domínio.

---

## 📊 Regras de anomalia

Nesta primeira versão foram definidas as seguintes regras:

| Grandeza | Condição considerada anômala |
|---|---|
| 🌡️ Temperatura | Acima de 30 °C |
| 💧 Umidade | Abaixo de 30% |
| 💡 Luminosidade | Abaixo de 100 lux |

Esses limites representam regras iniciais definidas para o projeto e poderão ser ajustados conforme sua evolução.

---

## 🧪 Testes unitários

Os testes foram desenvolvidos utilizando **JUnit 5**.

Atualmente a Semana 1 possui **7 testes unitários**, verificando:

1. Detecção de temperatura alta;
2. Temperatura dentro da condição normal;
3. Detecção de umidade baixa;
4. Umidade dentro da condição normal;
5. Detecção de luminosidade baixa;
6. Geração de um objeto `Alerta` quando uma anomalia é identificada;
7. Não geração de alerta quando a leitura está dentro da condição normal.

Resultado atual dos testes:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Para executar os testes:

```bash
mvn clean test
```

---

## 🏗️ Estrutura da Semana 1

```text
ninho/
│
├── .gitignore
├── AGENTS.md
├── README.md
├── pom.xml
│
└──src/
├── main/
│   └── java/
│       └── br/
│           └── com/
│               └── assistenteambiental/
│                   └── domain/
│                       ├── Alerta.java
│                       ├── AnalisadorDeAnomalias.java
│                       └── LeituraTelemetria.java
│
└── test/
    └── java/
        └── br/
            └── com/
                └── assistenteambiental/
                    └── domain/
                        └── AnalisadorDeAnomaliasTest.java
```

---

## 🛠️ Tecnologias utilizadas

- Java 25
- Maven
- JUnit 5
- Programação Orientada a Objetos
- TDD — Test Driven Development
- Git e GitHub para versionamento

---

## 📅 Cronograma de desenvolvimento

| Etapa | Desenvolvimento | Status |
|---|---|---|
| Semana 1 | Domínio e regras de anomalia (TDD) | ✅ Concluída |
| Semana 2 | Sensores concorrentes | ⏳ Pendente |
| Semana 3 | Fila compartilhada | ⏳ Pendente |
| Semana 4 | Persistência e log | ⏳ Pendente |
| Semana 5 | Concorrência sob estresse | ⏳ Pendente |
| Semana 6 | Testes finais e revisão | ⏳ Pendente |

---

## 🔜 Próxima etapa

Na **Semana 2**, o projeto será expandido com a implementação dos sensores simulados.

A classe `Sensor` será implementada utilizando `Runnable`, permitindo que diferentes sensores gerem leituras em threads separadas.

Essa etapa será desenvolvida posteriormente, seguindo o cronograma da atividade.

---

## 🎓 Contexto acadêmico

Projeto desenvolvido como atividade do **2º Bimestre da disciplina de Programação Orientada a Objetos**.

O projeto trabalha progressivamente conceitos como:

- Modelagem de domínio;
- Programação Orientada a Objetos;
- Test Driven Development;
- Testes unitários;
- Multithreading;
- Sincronização;
- Entrada e saída de arquivos;
- Persistência de dados com JDBC.

---

## 👨‍💻 Autor

**Guilherme Leal**
**RA: 25216067-2**

Projeto acadêmico desenvolvido para fins de aprendizagem e aplicação prática dos conceitos de Programação Orientada a Objetos.