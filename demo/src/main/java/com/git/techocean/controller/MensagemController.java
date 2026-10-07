package com.git.techocean.controller;

import com.git.techocean.model.Mensagem;
import com.git.techocean.repository.MensagemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
public class MensagemController {

    private final MensagemRepository mensagemRepository;

    public MensagemController(MensagemRepository mensagemRepository) {
        this.mensagemRepository = mensagemRepository;
    }

    //RF18 - Cliente envia mensagem
    @PostMapping
    public ResponseEntity<Mensagem> enviarMensagem(@RequestBody Mensagem mensagem) {
        Mensagem mensagemSalva = mensagemRepository.save(mensagem);
        return ResponseEntity.ok(mensagemSalva);
    }

    //RF19 - Funcionários visualizam mensagens
    @GetMapping
    public ResponseEntity<List<Mensagem>> listarMensagens() {
        List<Mensagem> mensagens = mensagemRepository.findAll();
        return ResponseEntity.ok(mensagens);
    }

    //RF20 - Funcionários responde as mensagens
    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> responderMensagem(@PathVariable Long id, @RequestBody Mensagem resposta) {
        return mensagemRepository.findById(id)
                .map(mensagem -> {
                    mensagem.setResposta(resposta.getResposta());
                    Mensagem mensagemAtualizada = mensagemRepository.save(mensagem);
                    return ResponseEntity.ok(mensagemAtualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}