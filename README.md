# Sistema de Empréstimo de Equipamentos

![CI](https://github.com/mariasilvano/emprestimos/actions/workflows/ci.yml/badge.svg)

API REST para controlar o empréstimo de equipamentos (notebooks, monitores, mouses etc.) a colaboradores de uma empresa. Projeto 2 do bootcamp #ElasTech.

**API no ar (Render):** https://emprestimos-uscn.onrender.com

> No plano gratuito do Render o serviço dorme após 15 minutos parado. A primeira requisição depois disso pode levar cerca de um minuto para responder. Os dados ficam em um banco H2, que é zerado a cada reinício ou novo deploy.

## Tecnologias

- Java 25
- Spring Boot 4.1.2 (Web MVC, Data JPA, Validation, Actuator)
- Banco H2
- Maven
- JUnit 5 e MockMvc
- Docker e Render (deploy)
- GitHub Actions (integração contínua)

## Como rodar

### Pré-requisito
JDK 25 instalado.

### Pelo Maven Wrapper
```bash
# Windows (PowerShell)
.\mvnw spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.

### Pelo Docker
```bash
docker build -t emprestimos .
docker run -p 8080:8080 emprestimos
```

### Testes
```bash
.\mvnw test
```
Os testes usam um banco H2 em memória (`src/test/resources/application.properties`) e não alteram o banco da aplicação.

### Console do H2 (somente local)
`http://localhost:8080/h2-console`, com a URL JDBC `jdbc:h2:file:./dados/emprestimos`. No Render o console fica desabilitado.

## Rotas

### Colaboradores

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| GET | `/colaboradores` | Lista todos | 200 |
| GET | `/colaboradores/{id}` | Busca por id | 200 |
| GET | `/colaboradores/{id}/emprestimos` | Histórico de empréstimos do colaborador | 200 |
| POST | `/colaboradores` | Cadastra um colaborador | 201 |

### Equipamentos

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| GET | `/equipamentos` | Lista todos. Filtros opcionais: `?tipo=NOTEBOOK` e `?disponivel=true` | 200 |
| GET | `/equipamentos/{id}` | Busca por id | 200 |
| POST | `/equipamentos` | Cadastra um equipamento | 201 |
| PUT | `/equipamentos/{id}` | Atualiza nome e tipo | 200 |
| DELETE | `/equipamentos/{id}` | Remove um equipamento | 204 |

Tipos aceitos: `NOTEBOOK`, `MONITOR`, `MOUSE`, `TECLADO`, `HEADSET`, `OUTRO`.

### Empréstimos

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| GET | `/emprestimos` | Lista todos. Com `?atrasados=true`, só os atrasados | 200 |
| GET | `/emprestimos/{id}` | Busca por id | 200 |
| POST | `/emprestimos?equipamentoId=1&colaboradorId=1` | Empresta um equipamento a um colaborador | 201 |
| PUT | `/emprestimos/{id}/devolucao` | Registra a devolução | 200 |

### Exemplos

```bash
# Cadastrar equipamento
curl -X POST https://emprestimos-uscn.onrender.com/equipamentos \
  -H "Content-Type: application/json" \
  -d '{"patrimonio":"NB-001","nome":"Notebook Dell","tipo":"NOTEBOOK"}'

# Cadastrar colaborador
curl -X POST https://emprestimos-uscn.onrender.com/colaboradores \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana","email":"ana@empresa.com","cargo":"Analista"}'

# Emprestar (os ids vão na URL, sem corpo)
curl -X POST "https://emprestimos-uscn.onrender.com/emprestimos?equipamentoId=1&colaboradorId=1"

# Devolver
curl -X PUT https://emprestimos-uscn.onrender.com/emprestimos/1/devolucao
```

## Regras de negócio

| Regra | Descrição |
|---|---|
| R1 | O patrimônio do equipamento é único |
| R2 | Todo equipamento novo nasce disponível |
| R3 | O e-mail do colaborador é único |
| R4 | Só se empresta equipamento disponível |
| R5 | O empréstimo registra a data de hoje e prevê a devolução para 14 dias depois; o equipamento fica indisponível |
| R6 | Um empréstimo já devolvido não pode ser devolvido de novo |
| R7 | A devolução registra a data de hoje e o equipamento volta a ficar disponível |
| R8 | Um empréstimo está atrasado se ainda não foi devolvido e a data prevista já passou |
| R9 | Equipamento que já teve empréstimo não pode ser removido |
| R10 | Colaborador com empréstimo atrasado não pode pegar outro equipamento |

## Respostas de erro

| Status | Quando |
|---|---|
| 400 | Dados inválidos (com a lista dos campos), tipo inexistente ou corpo inválido |
| 404 | Equipamento, colaborador ou empréstimo não encontrado |
| 409 | Violação de regra de negócio (R1, R3, R4, R6, R9, R10) |

## Bônus implementados

- **Swagger:** documentação interativa em `/swagger-ui.html`
- **Actuator:** saúde da aplicação em `/actuator/health`
- **Validação:** `@NotBlank`, `@Email` e `@NotNull`, com `400` listando os campos inválidos
- **Tratamento de erros:** `@RestControllerAdvice` com `404`, `409` e `400`
- **`@ManyToOne`:** o `Emprestimo` referencia `Equipamento` e `Colaborador` por objeto
- **Testes de integração com MockMvc:** 4 testes (cadastro, 404, emprestar e devolver, 409)
- **GitHub Actions:** os testes rodam a cada push e pull request (`.github/workflows/ci.yml`)

## Testes

- `EquipamentoTest`: 3 testes unitários (regra R4 e devolução)
- `EmprestimoTest`: 7 testes unitários (R5, R6, R7 e R8)
- `EmprestimoIntegracaoTest`: 4 testes de integração com MockMvc

## Estrutura

```
src/main/java/com/projeto/emprestimos/
├── controller/   rotas HTTP
├── service/      regras de negócio
├── repository/   acesso ao banco (Spring Data JPA)
├── model/        entidades (Equipamento, Colaborador, Emprestimo)
├── dto/          EmprestimoRequest e EmprestimoResponse
└── exception/    exceções e tratador de erros
```
## Autora
Maria Gabriela Silvano
