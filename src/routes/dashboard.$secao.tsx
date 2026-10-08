import { createFileRoute, notFound } from "@tanstack/react-router";
import { useEffect, useState, type FormEvent } from "react";
import { secoes } from "@/lib/dashboard-secoes";
import type { Carga as CargaModel } from "@/models/carga";
import { cadastrarCarga, listarCargas } from "@/services/cargas";
import type { Cliente as ClienteModel } from "@/models/cliente";
import { cadastrarCliente, listarClientes } from "@/services/clientes";
import {
  Conteineres,
  Mensagens as PainelMensagens,
  Operacoes,
  Orcamentos,
  Servicos,
  Solicitacoes,
  UsuariosAdmin,
} from "@/components/dashboard/painel-operacional";
import { RelatorioClientes } from "@/components/dashboard/relatorio-clientes";

export const Route = createFileRoute("/dashboard/$secao")({
  loader: ({ params }) => {
    const secao = secoes.find((s) => s.slug === params.secao);
    if (!secao) throw notFound();
    return { slug: secao.slug };
  },
  notFoundComponent: () => <p className="text-muted-foreground">Seção não encontrada.</p>,
  component: SecaoPage,
});

const campo = "w-full rounded-md border border-input bg-background px-3 py-2 text-sm";

function SecaoPage() {
  const { slug } = Route.useLoaderData();
  const secao = secoes.find((s) => s.slug === slug)!;
  return (
    <div>
      <p className="eyebrow mb-2">Painel</p>
      <h1 className="text-3xl font-bold">{secao.nome}</h1>
      <p className="mb-8 mt-2 text-muted-foreground">{secao.descricao}</p>
      {slug === "carga" ? (
        <Cargas />
      ) : slug === "cliente" ? (
        <Clientes />
      ) : slug === "conteiner" ? (
        <Conteineres />
      ) : slug === "mensagens" ? (
        <PainelMensagens />
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
        <div className="overflow-x-auto rounded-xl border border-border">
          <table className="w-full text-left text-sm">
            <thead className="bg-card text-muted-foreground">
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

function Clientes() {
  const [clientes, setClientes] = useState<ClienteModel[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  async function atualizarLista() {
    setClientes(await listarClientes());
  }

  useEffect(() => {
    let ativa = true;

    listarClientes()
      .then((resultado) => {
        if (ativa) setClientes(resultado);
      })
      .catch((error: unknown) => {
        if (ativa)
          setErro(
            error instanceof Error ? error.message : "Não foi possível carregar os clientes.",
          );
      })
      .finally(() => {
        if (ativa) setCarregando(false);
      });

    return () => {
      ativa = false;
    };
  }, []);

  async function cadastrar(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setErro("");
    setSucesso("");
    setSalvando(true);

    const form = e.currentTarget;
    const dados = new FormData(form);
    try {
      await cadastrarCliente({
        nome: String(dados.get("nome")).trim(),
        cpf: String(dados.get("cpfCnpj")).replace(/\D/g, ""),
        telefone: String(dados.get("telefone")).replace(/\D/g, ""),
        email: String(dados.get("email")).trim(),
        senha: String(dados.get("senha")),
      });
      await atualizarLista();
      setSucesso("Cliente cadastrado com sucesso.");
      form.reset();
    } catch (error) {
      setErro(error instanceof Error ? error.message : "Não foi possível cadastrar o cliente.");
    } finally {
      setSalvando(false);
    }
  }

  return (
    <div className="grid gap-8">
      <form
        onSubmit={cadastrar}
        className="grid max-w-3xl gap-4 rounded-xl border border-border bg-card p-5 sm:grid-cols-2"
      >
        <h2 className="text-lg font-semibold sm:col-span-2">Cadastrar cliente</h2>
        <label className="grid gap-1 text-sm">
          Nome
          <input name="nome" required maxLength={255} className={campo} />
        </label>
        <label className="grid gap-1 text-sm">
          CPF/CNPJ
          <input
            name="cpfCnpj"
            required
            inputMode="numeric"
            minLength={11}
            maxLength={18}
            className={campo}
          />
        </label>
        <label className="grid gap-1 text-sm">
          Telefone
          <input name="telefone" required type="tel" maxLength={20} className={campo} />
        </label>
        <label className="grid gap-1 text-sm">
          E-mail
          <input name="email" required type="email" maxLength={255} className={campo} />
        </label>
        <label className="grid gap-1 text-sm sm:col-span-2">
          Senha
          <input name="senha" required type="password" minLength={8} className={campo} />
        </label>
        <button
          disabled={salvando}
          className="rounded-full bg-primary px-6 py-3 text-sm font-semibold text-primary-foreground disabled:opacity-60 sm:col-span-2 sm:justify-self-start"
        >
          {salvando ? "Cadastrando..." : "Cadastrar cliente"}
        </button>
        {erro && (
          <p role="alert" className="text-sm text-destructive sm:col-span-2">
            {erro}
          </p>
        )}
        {sucesso && (
          <p role="status" className="text-sm text-primary sm:col-span-2">
            {sucesso}
          </p>
        )}
      </form>

      <section>
        <h2 className="mb-3 text-lg font-semibold">Clientes cadastrados</h2>
        {carregando ? (
          <p className="text-sm text-muted-foreground">Carregando clientes...</p>
        ) : clientes.length === 0 ? (
          <p className="text-sm text-muted-foreground">Nenhum cliente cadastrado.</p>
        ) : (
          <div className="overflow-x-auto rounded-xl border border-border">
            <table className="w-full text-left text-sm">
              <thead className="bg-card text-muted-foreground">
                <tr>
                  {["Código", "Nome", "CPF/CNPJ", "Telefone", "E-mail"].map((titulo) => (
                    <th key={titulo} className="px-4 py-3 font-medium">
                      {titulo}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {clientes.map((cliente) => (
                  <tr key={cliente.id} className="border-t border-border">
                    <td className="px-4 py-3">{cliente.id}</td>
                    <td className="px-4 py-3">{cliente.nome}</td>
                    <td className="px-4 py-3">{cliente.cpfCnpj}</td>
                    <td className="px-4 py-3">{cliente.telefone}</td>
                    <td className="px-4 py-3">{cliente.email}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}

function Cargas() {
  const [cargas, setCargas] = useState<CargaModel[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  useEffect(() => {
    let ativa = true;

    listarCargas()
      .then((resultado) => {
        if (ativa) setCargas(resultado);
      })
      .catch((error: unknown) => {
        if (ativa)
          setErro(error instanceof Error ? error.message : "Não foi possível carregar as cargas.");
      })
      .finally(() => {
        if (ativa) setCarregando(false);
      });

    return () => {
      ativa = false;
    };
  }, []);

  async function cadastrar(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setErro("");
    setSucesso("");
    setSalvando(true);

    const form = e.currentTarget;
    const dados = new FormData(form);
    try {
      const novaCarga = await cadastrarCarga({
        descricao: String(dados.get("descricao")).trim(),
        peso: Number(dados.get("peso")),
        volume: Number(dados.get("volume")),
        tipoCarga: String(dados.get("tipoCarga")).trim(),
        origem: String(dados.get("origem")).trim(),
        destino: String(dados.get("destino")).trim(),
      });
      setCargas((atuais) => [...atuais, novaCarga]);
      setSucesso("Carga cadastrada com sucesso.");
      form.reset();
    } catch (error) {
      setErro(error instanceof Error ? error.message : "Não foi possível cadastrar a carga.");
    } finally {
      setSalvando(false);
    }
  }

  return (
    <div className="grid gap-8">
      <form
        onSubmit={cadastrar}
        className="grid max-w-3xl gap-4 rounded-xl border border-border bg-card p-5 sm:grid-cols-2"
      >
        <h2 className="text-lg font-semibold sm:col-span-2">Cadastrar carga</h2>
        <label className="grid gap-1 text-sm sm:col-span-2">
          Descrição
          <input name="descricao" required maxLength={255} className={campo} />
        </label>
        <label className="grid gap-1 text-sm">
          Peso (kg)
          <input name="peso" type="number" min="0.01" step="any" required className={campo} />
        </label>
        <label className="grid gap-1 text-sm">
          Volume (m³)
          <input name="volume" type="number" min="0.01" step="any" required className={campo} />
        </label>
        <label className="grid gap-1 text-sm">
          Tipo de carga
          <input name="tipoCarga" required maxLength={100} className={campo} />
        </label>
        <label className="grid gap-1 text-sm">
          Origem
          <input name="origem" required maxLength={255} className={campo} />
        </label>
        <label className="grid gap-1 text-sm sm:col-span-2">
          Destino
          <input name="destino" required maxLength={255} className={campo} />
        </label>
        <button
          disabled={salvando}
          className="rounded-full bg-primary px-6 py-3 text-sm font-semibold text-primary-foreground disabled:opacity-60 sm:col-span-2 sm:justify-self-start"
        >
          {salvando ? "Cadastrando..." : "Cadastrar carga"}
        </button>
        {erro && (
          <p role="alert" className="text-sm text-destructive sm:col-span-2">
            {erro}
          </p>
        )}
        {sucesso && (
          <p role="status" className="text-sm text-primary sm:col-span-2">
            {sucesso}
          </p>
        )}
      </form>

      <section>
        <h2 className="mb-3 text-lg font-semibold">Cargas cadastradas</h2>
        {carregando ? (
          <p className="text-sm text-muted-foreground">Carregando cargas...</p>
        ) : cargas.length === 0 ? (
          <p className="text-sm text-muted-foreground">Nenhuma carga cadastrada.</p>
        ) : (
          <div className="overflow-x-auto rounded-xl border border-border">
            <table className="w-full text-left text-sm">
              <thead className="bg-card text-muted-foreground">
                <tr>
                  {[
                    "Código",
                    "Descrição",
                    "Peso (kg)",
                    "Volume (m³)",
                    "Tipo",
                    "Origem",
                    "Destino",
                  ].map((titulo) => (
                    <th key={titulo} className="px-4 py-3 font-medium">
                      {titulo}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {cargas.map((carga) => (
                  <tr key={carga.id} className="border-t border-border">
                    <td className="px-4 py-3">{carga.id}</td>
                    <td className="px-4 py-3">{carga.descricao}</td>
                    <td className="px-4 py-3">{carga.peso}</td>
                    <td className="px-4 py-3">{carga.volume}</td>
                    <td className="px-4 py-3">{carga.tipoCarga}</td>
                    <td className="px-4 py-3">{carga.origem}</td>
                    <td className="px-4 py-3">{carga.destino}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}
