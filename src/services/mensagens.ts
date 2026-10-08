import type { Mensagem } from "@/models/mensagem";
import { requisicaoApi } from "@/services/api";

export const listarMensagensCliente = (clienteId: number) =>
  requisicaoApi<Mensagem[]>(`/mensagens/cliente/${clienteId}`);

export const responderMensagem = (id: number, resposta: string) =>
  requisicaoApi<Mensagem>(`/mensagens/${id}/resposta`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ resposta }),
  });
