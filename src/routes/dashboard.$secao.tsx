import { createFileRoute, notFound } from "@tanstack/react-router";
import { useEffect, useState, type FormEvent } from "react";
import { secoes } from "@/lib/dashboard-secoes";
import type { Carga as CargaModel } from "@/models/carga";
import { cadastrarCarga, listarCargas } from "@/services/cargas";
import type { Cliente as ClienteModel } from "@/models/cliente";
import { cadastrarCliente, listarClientes } from "@/services/clientes";
import {
  Conteineres,
  Operacoes,
  Orcamentos,
  Servicos,
  Solicitacoes,
  UsuariosAdmin,
} from "@/components/dashboard/painel-operacional";

export const Route = createFileRoute("/dashboard/$secao")({
  loader: ({ params }) => {
    const secao = secoes.find((s) => s.slug === params.secao);
    if (!secao) throw notFound();
    return { slug: secao.slug };
  },
  notFoundComponent: () => (
    <p className="text-muted-foreground">Seção não encontrada.</p>
  ),
  component: SecaoPage,
});

const campo =
  "w-full rounded-md border border-input bg-background px-3 py-2 text-sm";

function SecaoPage() {
  const { slug } = Route.useLoaderData();
  const secao = secoes.find((s) => s.slug === slug)!;

  return (
    <div>
      <p className="eyebrow mb-2">Painel</p>
      <h1 className="text-3xl font-bold">{secao.nome}</h1>
      <p className="mb-8 mt-2 text-muted-foreground">{secao.descricao}</p>

      {/* Controller Central por Slug */}
      {slug === "carga" ? (
        <Cargas />
      ) : slug === "cliente" ? (
        <Clientes />
      ) : slug === "conteiner" ? (
        <Conteineres />
      ) : slug === "mensagens" ? (
        <Mensagens />
      ) : slug === "operacao" ? (
        <Operacoes />
      ) : slug === "orcamento" ? (
        <Orcamentos />
      ) : slug === "usuario" ? (
        <UsuariosAdmin />
      ) : slug === "servico" ? (
        <Servicos />
      ) : slug === "solicitacao" ? (
        <Solicitacoes />
      ) : slug === "relatorio" ? (
        <RelatorioClientes />
      ) : (
        <div className="overflow-x-auto rounded-xl border border-border bg-card">
          <table className="w-full text-left text-sm">
            <thead className="bg-muted/50 text-muted-foreground">
              <tr>
                {secao.colunas.map((c) => (
                  <th key={c} className="px-4 py-3 font-medium">
                    {c}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {secao.exemplos.map((linha, i) => (
                <tr key={i} className="border-t border-border">
                  {linha.map((v, j) => (
                    <td key={j} className="px-4 py-3">
                      {v}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

/* ==========================================================================
   1. RELATÓRIO DE CLIENTES (Interativo com Modal de Detalhes e Ações)
   ========================================================================== */
function RelatorioClientes() {
  const [clientes, setClientes] = useState<ClienteModel[]>([
    {
      id: 1,
      nome: "Carlos Lima",
      cpfCnpj: "123.456.789-00",
      telefone: "(41) 9999-8888",
      email: "carlos@empresa.com",
    },
    {
      id: 2,
      nome: "Ana Souza",
      cpfCnpj: "987.654.321-00",
      telefone: "(41) 9888-7777",
      email: "ana@logistica.com",
    },
  ]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");

  // Estado para controlar o modal do cliente selecionado
  const [clienteSelecionado, setClienteSelecionado] = useState<ClienteModel | null>(null);

  async function carregarDados() {
    setCarregando(true);
    setErro("");
    try {
      const dados = await listarClientes();
      if (dados && dados.length > 0) {
        setClientes(dados);
      }
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Não foi possível carregar o relatório."
      );
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregarDados();
  }, []);

  function handleImprimir() {
    window.print();
  }

  function handleDeletar(e: React.MouseEvent, id: number | string) {
    e.stopPropagation(); // Impede de abrir o modal ao clicar diretamente em deletar
    if (confirm("Tem certeza que deseja excluir este cliente do relatório?")) {
      setClientes((atuais) => atuais.filter((cliente) => cliente.id !== id));
      if (clienteSelecionado?.id === id) {
        setClienteSelecionado(null);
      }
    }
  }

  function handleDeletarTudo() {
    if (confirm("Tem certeza que deseja limpar todos os registros deste relatório?")) {
      setClientes([]);
      setClienteSelecionado(null);
    }
  }

  return (
    <div className="space-y-6">
      {/* Barra de Ações Superior (Oculta ao imprimir) */}
      <div className="flex flex-wrap items-center justify-between gap-4 rounded-xl border border-border bg-card p-4 print:hidden">
        <div>
          <h2 className="text-base font-semibold">Ações do Relatório</h2>
          <p className="text-xs text-muted-foreground">
            Clique em qualquer linha da tabela para abrir o cadastro detalhado.
          </p>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          {/* Botão Atualizar */}
          <button
            type="button"
            onClick={carregarDados}
            disabled={carregando}
            className="rounded-lg border border-border bg-background px-4 py-2 text-xs font-medium transition-colors hover:bg-muted disabled:opacity-50"
          >
            {carregando ? "Atualizando..." : "🔄 Atualizar"}
          </button>

          {/* Botão Limpar Relatório */}
          <button
            type="button"
            onClick={handleDeletarTudo}
            disabled={clientes.length === 0}
            className="rounded-lg bg-destructive/10 px-4 py-2 text-xs font-semibold text-destructive transition-colors hover:bg-destructive hover:text-destructive-foreground disabled:opacity-50"
          >
            🗑️ Limpar Relatório
          </button>

          {/* Botão Imprimir / PDF */}
          <button
            type="button"
            onClick={handleImprimir}
            className="rounded-lg bg-primary px-4 py-2 text-xs font-semibold text-primary-foreground transition-opacity hover:opacity-90"
          >
            🖨️ Imprimir / Salvar PDF
          </button>
        </div>
      </div>

      {/* Tabela de Relatório */}
      <section>
        {erro && (
          <p role="alert" className="mb-4 text-sm text-destructive">
            {erro}
          </p>
        )}

        {carregando ? (
          <p className="text-sm text-muted-foreground">
            Carregando dados do relatório...
          </p>
        ) : clientes.length === 0 ? (
          <p className="text-sm text-muted-foreground">
            Nenhum registro encontrado no relatório.
          </p>
        ) : (
          <div className="overflow-x-auto rounded-xl border border-border bg-card">
            <table className="w-full text-left text-sm">
              <thead className="bg-muted/50 text-muted-foreground">
                <tr>
                  <th className="px-4 py-3 font-medium">Código</th>
                  <th className="px-4 py-3 font-medium">Nome</th>
                  <th className="px-4 py-3 font-medium">CPF/CNPJ</th>
                  <th className="px-4 py-3 font-medium">Telefone</th>
                  <th className="px-4 py-3 font-medium">E-mail</th>
                  <th className="px-4 py-3 font-medium text-right print:hidden">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody>
                {clientes.map((cliente) => (
                  <tr
                    key={cliente.id}
                    onClick={() => setClienteSelecionado(cliente)}
                    className="cursor-pointer border-t border-border transition-colors hover:bg-muted/50"
                  >
                    <td className="px-4 py-3 font-mono text-xs font-semibold text-primary">
                      #{cliente.id}
                    </td>
                    <td className="px-4 py-3 font-medium">{cliente.nome}</td>
                    <td className="px-4 py-3">{cliente.cpfCnpj}</td>
                    <td className="px-4 py-3">{cliente.telefone}</td>
                    <td className="px-4 py-3">{cliente.email}</td>
                    <td className="px-4 py-3 text-right print:hidden">
                      {/* Botão Deletar por linha */}
                      <button
                        type="button"
                        onClick={(e) => handleDeletar(e, cliente.id)}
                        className="rounded bg-destructive/10 px-2.5 py-1 text-xs font-semibold text-destructive transition-colors hover:bg-destructive hover:text-destructive-foreground"
                      >
                        🗑️ Deletar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      {/* Modal / Pop-up de Detalhes do Cliente */}
      {clienteSelecionado && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4 backdrop-blur-sm print:hidden">
          <div className="w-full max-w-lg rounded-xl border border-border bg-card p-6 shadow-2xl">
            <div className="flex items-center justify-between border-b border-border pb-3">
              <div>
                <span className="text-xs font-semibold uppercase text-primary">
                  Ficha do Cliente #{clienteSelecionado.id}
                </span>
                <h3 className="text-xl font-bold">{clienteSelecionado.nome}</h3>
              </div>
              <button
                type="button"
                onClick={() => setClienteSelecionado(null)}
                className="rounded-lg p-1 text-muted-foreground hover:bg-muted hover:text-foreground"
              >
                ✖
              </button>
            </div>

            <div className="mt-4 space-y-3 text-sm">
              <div className="grid grid-cols-2 gap-3 rounded-lg border border-border bg-muted/20 p-3">
                <div>
                  <p className="text-xs text-muted-foreground">CPF / CNPJ</p>
                  <p className="font-medium">{clienteSelecionado.cpfCnpj}</p>
                </div>
                <div>
                  <p className="text-xs text-muted-foreground">Telefone</p>
                  <p className="font-medium">{clienteSelecionado.telefone}</p>
                </div>
              </div>

              <div>
                <p className="text-xs text-muted-foreground">E-mail de Contato</p>
                <p className="font-medium">{clienteSelecionado.email}</p>
              </div>

              <div className="rounded-lg border border-border bg-muted/10 p-3">
                <p className="mb-1 text-xs font-semibold text-muted-foreground">
                  Resumo de Atividades
                </p>
                <p className="text-xs text