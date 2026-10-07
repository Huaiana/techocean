package com.git.techocean.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.git.techocean.model.Cliente;
import com.git.techocean.repository.ClienteRepository;
import com.git.techocean.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    // RF01 - Cadastrar cliente
    @PostMapping
    public Cliente cadastrar(
            @RequestParam String nome,
            @RequestParam String cpfcnpj,
            @RequestParam String email,
            @RequestParam String senha) {
        return clienteService.cadastrar(nome, cpfcnpj, email, senha);
    }

    // RF02 - Login de cliente
    @PostMapping("/login")
    public ResponseEntity<Cliente> loginCliente(@RequestBody Cliente cliente) {
        Cliente clienteEncontrado = clienteRepository.findByEmail(cliente.getEmail());
        if (clienteEncontrado != null && clienteEncontrado.getSenha().equals(cliente.getSenha())) {
            return new ResponseEntity<>(clienteEncontrado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // RF21 - Administradores podem visualizar clientes cadastrados
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    // RF22 - Administradores podem editar ou atualizar dados de clientes cadastrados
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
        if (clienteExistente != null) {
            clienteExistente.setNome(clienteAtualizado.getNome());
            clienteExistente.setEmail(clienteAtualizado.getEmail());
            clienteExistente.setCPFCNPJ(clienteAtualizado.getCPFCNPJ());
            Cliente clienteSalvo = clienteRepository.save(clienteExistente);
            return new ResponseEntity<>(clienteSalvo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }   
}