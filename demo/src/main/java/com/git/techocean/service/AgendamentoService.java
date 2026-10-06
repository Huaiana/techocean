package com.git.techocean.service;

import com.git.techocean.model.Agendamento;
import com.git.techocean.model.Usuario;
import com.git.techocean.repository.AgendamentoRepository;
import com.git.techocean.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            UsuarioRepository usuarioRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Agendamento solicitarVisita(Agendamento request) {
        if (request == null
                || isBlank(request.getNome())
                || isBlank(request.getTelefone())
                || isBlank(request.getEmail())
                || request.getDataHora() == null
                || !request.isConfirmacao()) {
            throw new AgendamentoException(
                    "DADOS_INVALIDOS",
                    "Preencha os campos obrigatórios e confirme a solicitação.",
                    HttpStatus.BAD_REQUEST);
        }

        if (!request.getDataHora().isAfter(LocalDateTime.now())) {
            throw new AgendamentoException(
                    "DATA_INVALIDA",
                    "A data e o horário da visita devem ser futuros.",
                    HttpStatus.BAD_REQUEST);
        }

        String email = request.getEmail().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email);

        if (usuario == null) {
            if (isBlank(request.getCpf()) || isBlank(request.getSenha())) {
                throw new AgendamentoException(
                        "CADASTRO_NECESSARIO",
                        "Você ainda não tem cadastro. Cadastre seus dados para continuar com o agendamento.",
                        HttpStatus.CONFLICT);
            }
            if (request.getSenha().length() < 8) {
                throw new AgendamentoException(
                        "SENHA_INVALIDA",
                        "A senha precisa ter pelo menos 8 caracteres.",
                        HttpStatus.BAD_REQUEST);
            }

            usuario = new Usuario();
            usuario.setNome(request.getNome().trim());
            usuario.setCPF(request.getCpf().trim());
            usuario.setEmail(email);
            usuario.setSenha(request.getSenha());
            usuario = usuarioRepository.save(usuario);
        } else if (isBlank(request.getSenha())) {
            throw new AgendamentoException(
                    "SENHA_NECESSARIA",
                    "Informe sua senha de usuário para confirmar o agendamento.",
                    HttpStatus.UNAUTHORIZED);
        } else if (!Objects.equals(usuario.getSenha(), request.getSenha())) {
            throw new AgendamentoException(
                    "SENHA_INVALIDA",
                    "A senha não corresponde ao cadastro deste e-mail.",
                    HttpStatus.UNAUTHORIZED);
        }

        request.setEmail(email);
        request.setTelefone(request.getTelefone().trim());
        request.setConfirmado(true);
        request.setUsuario(usuario);
        return agendamentoRepository.save(request);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
