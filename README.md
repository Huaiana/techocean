# Techocean

Sistema para apresentar serviços de segurança e amarração de cargas e disponibilizar uma API REST de apoio à operação.

## Visão geral

O repositório contém duas aplicações:

- **Site institucional** (`front-buddy-dev/`): página inicial com apresentação dos serviços, informações da empresa e contato.
- **API** (`demo/`): aplicação Spring Boot para cadastro de clientes e usuários, gerenciamento de serviços, orçamentos e agendamentos de visitas.

O site e a API são executados separadamente. A página institucional atual não depende da API para renderizar seu conteúdo.

## Tecnologias

- **Site:** React, TypeScript, TanStack Start/Router, Vite e Tailwind CSS.
- **API:** Java 17, Spring Boot, Spring Web MVC e Spring Data JPA.
- **Persistência local:** dependência H2 incluída no backend; as configurações da aplicação estão em `demo/src/main/resources/application.properties`.

## Estrutura

```text
.
├── demo/                 # API Java/Spring Boot e testes
└── front-buddy-dev/      # Site institucional React
```

## Pré-requisitos

- Java 17 ou superior.
- Node.js e npm para o site.

O backend usa o Maven Wrapper (`mvnw` / `mvnw.cmd`), portanto não é necessário instalar o Maven globalmente.

## Executar localmente

### API

No Windows, a partir da raiz do repositório:

```powershell
cd demo
.\mvnw.cmd spring-boot:run
```

No macOS ou Linux:

```bash
cd demo
./mvnw spring-boot:run
```

A API Spring Boot fica disponível, por padrão, em `http://localhost:8080`. Essa porta serve os endpoints da API, não o site; abrir `http://localhost:8080/` pode retornar `404`. Para verificar a API, acesse `http://localhost:8080/clientes`.

### Site

Em outro terminal:

```bash
cd front-buddy-dev
npm install
npm run dev
```

Use o endereço local informado pelo Vite no terminal para abrir o site. Como a porta `8080` é usada pela API, o Vite escolhe outra porta disponível (por exemplo, `8081`) quando ambos estão em execução. Para gerar e visualizar a versão de produção:

```bash
npm run build
npm run preview
```

## API REST

Os endpoints abaixo são definidos pelos controladores do backend:

| Método | Endpoint | Finalidade |
| --- | --- | --- |
| `POST` | `/clientes` | Cadastrar cliente |
| `POST` | `/clientes/login` | Autenticar cliente |
| `GET` | `/clientes` | Listar clientes |
| `PUT` | `/clientes/{id}` | Atualizar dados do cliente |
| `POST` | `/usuarios` | Cadastrar usuário |
| `POST` | `/usuarios/login` | Autenticar funcionário |
| `POST` | `/servicos` | Cadastrar serviço |
| `GET` | `/servicos/listar` | Listar serviços |
| `PUT` | `/servicos/{id}` | Atualizar serviço |
| `DELETE` | `/servicos/deletar/{id}` | Excluir serviço |
| `POST` | `/orcamentos/solicitar` | Solicitar orçamento; inicia com status `pendente` |
| `GET` | `/orcamentos/consultar/{clienteId}` | Consultar orçamentos de um cliente |
| `PUT` | `/orcamentos/{id}/status?status={status}` | Alterar o status de um orçamento |
| `POST` | `/agendamentos` | Solicitar uma visita técnica; cria o cadastro em `Usuario` quando os dados de registro são enviados |

As operações que recebem dados esperam um corpo JSON. Para agendar uma visita, envie `nome`, `telefone`, `email`, `dataHora` (ISO local, por exemplo `2026-10-20T14:30`) e `confirmacao: true`. Se o e-mail já estiver cadastrado em `Usuario`, envie também `senha`. Se não houver cadastro, a API informa que o cadastro é necessário; o site encaminha o visitante para criar o usuário com CPF e senha e conclui o agendamento em seguida. A atualização de status do orçamento recebe o novo status no parâmetro `status`.

## Testes e verificações

Executar os testes do backend:

```powershell
cd demo
.\mvnw.cmd test
```

Executar os testes do site:

```bash
cd front-buddy-dev
npm test
```

Verificar o estilo do site:

```bash
cd front-buddy-dev
npm run lint
```
