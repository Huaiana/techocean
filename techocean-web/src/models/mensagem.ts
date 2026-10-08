import type { Cliente } from "@/models/cliente";

export type Mensagem = {
  id: number;
  cliente: Cliente;
  especialista: string;
  conteudo: string;
  resposta: string | null;
  status: string;
};
