# Techocean

Sistema para apresentar serviços de segurança e amarração de cargas e disponibilizar uma API REST de apoio à operação.

## Visão geral

O repositório contém duas aplicações:

- **Site institucional** (raiz do repositório): página inicial com apresentação dos serviços, informações da empresa e contato.
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
└── src/                  # Site institucional React
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

A API Spring Boot fica disponível, por padrão, em `http://localhost:8080`. O Vite usa a porta `5173` por padrão, mas a porta pode ser definida pelo comando que o inicia. Se o Vite já estiver usando `8080`, inicie a API em `8081`:

```powershell
cd demo
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Nesse caso, `VITE_API_URL` deve apontar para `http://localhost:8081` (como configurado neste checkout). Para confirmar que está acessando a API — e não o servidor do site — teste `http://localhost:8081/clientes`; a resposta esperada é `200`. A raiz `/` não é uma rota da API e pode retornar `404`.

### Site

Em outro terminal:

```bash
npm install
npm run dev
```

Use o endereço local informado pelo Vite no terminal para abrir o site. Se usar a porta `8080` para o Vite, mantenha a API em `8081` e `VITE_API_URL=http://localhost:8081`. O Vite pode carregar as variáveis de ambiente ao iniciar; reinicie-o após alterar `.env`. Para gerar e visualizar a versão de produção:

```bash
npm run build
npm run preview
```

### Publicar a API no Render

O arquivo `render.yaml` prepara a API Spring Boot e um banco PostgreSQL de teste no Render:

1. No Render, crie um Blueprint a partir do repositório `Huaiana/techocean` e confirme a criação dos recursos definidos em `render.yaml`.
2. Aguarde o serviço `techocean-api` ficar disponível. O arquivo `.env.production` configura o build de produção do site para usar `https://techocean-api.onrender.com`; se o Render mostrar outro domínio para o serviço, atualize esse arquivo antes de publicar o site.
3. Depois de conectar o projeto Lovable ao GitHub na branch `main`, sincronize as alterações e publique novamente o projeto para que ele use essa URL.

O deploy usa os planos gratuitos do Render para teste. Serviços gratuitos podem suspender após inatividade e o banco gratuito é temporário; não use essa configuração para dados importantes ou de produção. O banco criado no Render começa vazio: dados gravados apenas no H2 local não são copiados.

Para desenvolvimento local depois da publicação, mantenha `VITE_API_URL` em `.env` apontando para a URL local da API (`http://localhost:8080` ou `http://localhost:8081`, conforme a porta usada). Builds de produção usam `.env.production`; valores de `VITE_API_URL` configurados diretamente no ambiente do provedor têm precedência sobre os arquivos `.env`.

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
npm test
```

Verificar o estilo do site:

```bash
npm run lint
```
