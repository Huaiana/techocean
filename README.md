# Techocean

Sistema para apresentar serviços de segurança e amarração de cargas e disponibilizar uma API REST de apoio à operação.

## Visão geral

O repositório contém duas aplicações:

- **Site institucional** (`techocean-web/`): página inicial com apresentação dos serviços, informações da empresa e contato.
- **API** (`demo/`): aplicação Spring Boot para clientes e administradores, cargas, serviços, contêineres, solicitações, orçamentos, operações, mensagens e agendamentos de visitas.

O site e a API são executados separadamente. A página institucional atual não depende da API para renderizar seu conteúdo.

## Tecnologias

- **Site:** React, TypeScript, TanStack Start/Router, Vite e Tailwind CSS.
- **API:** Java 17, Spring Boot, Spring Web MVC e Spring Data JPA.
- **Persistência local:** dependência H2 incluída no backend; as configurações da aplicação estão em `demo/src/main/resources/application.properties`.

## Estrutura

```text
.
├── demo/                 # API Java/Spring Boot e testes
└── techocean-web/        # Site institucional React
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

A API Spring Boot fica disponível, por padrão, em `http://localhost:8080`. Se essa porta já estiver ocupada no seu ambiente, a aplicação também pode rodar em `http://localhost:8081`; nesse caso, ajuste `VITE_API_URL` no site para apontar para a porta correta. Essa porta serve os endpoints da API, não o site; abrir `http://localhost:8080/` pode retornar `404`. Para verificar a API, acesse `http://localhost:8080/clientes` (ou `http://localhost:8081/clientes` se a porta foi remapeada).

### Site

Em outro terminal:

```bash
cd techocean-web
npm install
npm run dev
```

Use o endereço local informado pelo Vite no terminal para abrir o site. Como a porta `8080` é usada pela API, o Vite escolhe outra porta disponível (por exemplo, `8081`) quando ambos estão em execução. Para gerar e visualizar a versão de produção:

```bash
npm run build
npm run preview
```

## API REST

Os endpoints abaixo correspondem aos controladores atuais do backend. Salvo indicação em contrário, os valores de criação e atualização são recebidos como parâmetros de requisição (`@RequestParam`).

| Método | Endpoint | Finalidade |
| --- | --- | --- |
| `POST` | `/clientes` | Cadastrar cliente (corpo JSON com `nome`, `cpf`, `telefone`, `email` e `senha`) |
| `POST` | `/clientes/login` | Autenticar cliente (corpo JSON com `email` e `senha`) |
| `GET` | `/clientes` | Listar clientes |
| `GET` | `/clientes/{id}` | Buscar cliente por ID |
| `PUT` | `/clientes/{id}` | Atualizar nome, telefone, e-mail e CPF/CNPJ (corpo JSON) |
| `POST` | `/admin/cadastrar?nome=...&email=...&senha=...&cargo=...` | Cadastrar administrador |
| `POST` | `/admin/login` | Autenticar administrador (corpo JSON com `email` e `senha`) |
| `POST` | `/cargas?descricao=...&peso=...&volume=...&tipoCarga=...&origem=...&destino=...` | Cadastrar carga |
| `GET` | `/cargas` | Listar cargas |
| `GET` | `/cargas/{id}` | Buscar carga por ID |
| `POST` | `/servicos?nome=...&descricao=...&categoria=...&disponivel=...` | Cadastrar serviço |
| `GET` | `/servicos` | Listar serviços disponíveis |
| `GET` | `/servicos/{id}` | Buscar serviço por ID |
| `POST` | `/conteineres?numeroConteiner=...&tipo=...&situacao=...` | Cadastrar contêiner |
| `GET` | `/conteineres` | Listar contêineres |
| `GET` | `/conteineres/{id}` | Buscar contêiner por ID |
| `POST` | `/solicitacoes?clienteId=...&cargaId=...&servicoId=...` | Criar solicitação |
| `GET` | `/solicitacoes` | Listar solicitações |
| `GET` | `/solicitacoes/cliente/{clienteId}` | Listar solicitações de um cliente |
| `GET` | `/solicitacoes/{id}` | Buscar solicitação por ID |
| `PUT` | `/solicitacoes/{id}/status?status=...` | Atualizar status de solicitação |
| `POST` | `/orcamentos?clienteId=...&cargaId=...&servicoId=...` | Criar orçamento com status `PENDENTE` |
| `GET` | `/orcamentos` | Listar orçamentos |
| `GET` | `/orcamentos/cliente/{clienteId}` | Listar orçamentos de um cliente |
| `PUT` | `/orcamentos/{id}/analise?status=...&valorEstimado=...&observacoes=...` | Atualizar análise do orçamento |
| `POST` | `/operacoes?solicitacaoId=...&conteinerId=...&responsavel=...&observacoes=...` | Iniciar operação |
| `GET` | `/operacoes` | Listar operações |
| `PUT` | `/operacoes/{id}/andamento?andamento=...&observacoes=...` | Atualizar andamento da operação |
| `POST` | `/mensagens?clienteId=...&especialista=...&conteudo=...` | Enviar mensagem ao atendimento |
| `GET` | `/mensagens/cliente/{clienteId}` | Listar mensagens de um cliente |
| `PUT` | `/mensagens/{id}/resposta` | Responder mensagem (corpo JSON com `resposta`) |
| `POST` | `/agendamentos` | Solicitar visita técnica (corpo JSON) |

O formulário de visita do site envia a solicitação para `POST /agendamentos` usando a variável `VITE_API_URL`; por padrão, usa `http://localhost:8080`. Envie `nome`, `telefone`, `email`, `dataHora` (data e hora ISO local, por exemplo `2026-10-20T14:30`) e `confirmacao: true`. Para um cliente já cadastrado, inclua `senha`; para um novo cadastro, inclua também `cpf` e uma senha com pelo menos oito caracteres. O restante dos endpoints não está automaticamente integrado ao site.

## Testes e verificações

Executar os testes do backend:

```powershell
cd demo
.\mvnw.cmd test
```

Executar os testes do site:

```bash
cd techocean-web
npm test
```

Verificar o estilo do site:

```bash
cd techocean-web
npm run lint
```
# techocean
