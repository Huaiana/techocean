package com.git.techocean.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.git.techocean.model.Mensagem;
import com.git.techocean.service.MensagemService;

@RestController
@RequestMapping("/mensagens")
public class MensagemController {

    private final MensagemService mensagemService;

    public MensagemController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    // RF08 - Enviar mensagem ao atendimento
    @PostMapping
    public ResponseEntity<Mensagem> enviarMensagem(
            @RequestParam Long clienteId,
            @RequestParam(required = false) String especialista,
            @RequestParam String conteudo) {
        Mensagem mensagem = mensagemService.enviarMensagem(clienteId, especialista, conteudo);
        return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Mensagem>> listarPorCliente(@PathVariable Long clienteId) {
        return new ResponseEntity<>(mensagemService.listarPorCliente(clienteId), HttpStatus.OK);
    }
}