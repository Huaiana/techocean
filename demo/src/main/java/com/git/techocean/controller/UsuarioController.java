package com.git.techocean.controller;

import com.git.techocean.model.Usuario;
import com.git.techocean.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //RF04 - Cadastrar funcionario
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioRepository.save(usuario);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    //RF03 - Login de Administradores/Funcionarios
    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.findByEmailAndSenha(usuario.getEmail(), usuario.getSenha())
                .map(u -> ResponseEntity.ok("Login efetuado com sucesso"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas"));
    }
}
