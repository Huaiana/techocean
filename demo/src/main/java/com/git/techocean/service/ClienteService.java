package com.git.techocean.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import com.git.techocean.model.Cliente;
import com.git.techocean.repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(
        String nome,
        String cpfCnpj,
        String email,
        String senha) {
        validarCamposObrigatorios(nome, cpfCnpj, email, senha);
        
        return clienteRepository.save(new Cliente(nome, cpfCnpj, email, senha));
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente " + id + " não encontrado"));
    }

    // --------- regras de validacao ------------
    private void validarCamposObrigatorios(String nome, String cpfCnpj, String email, String senha) {
        if (nome == null || nome.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'nome' é obrigatório.");
        }
        if (cpfCnpj == null || cpfCnpj.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'cpfCnpj' é obrigatório.");
        }
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'email' é obrigatório.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'senha' é obrigatório.");
        }
    }

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
