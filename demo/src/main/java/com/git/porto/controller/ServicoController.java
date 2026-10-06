package com.git.porto.controller;

import com.git.porto.model.Servico;
import com.git.porto.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public final class ServicoController {

    private final ServicoRepository servicoRepository;

    public ServicoController(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    // RF05 / RF23 - Cadastrar/gerenciar serviço
    @PostMapping
    public ResponseEntity<Servico> cadastrarServico(@RequestBody Servico servico) {
        Servico novoServico = servicoRepository.save(servico);
        return new ResponseEntity<>(novoServico, HttpStatus.CREATED);
    }

    // RF06 - Consultar serviços disponíveis
    @GetMapping("/listar")
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        return new ResponseEntity<>(servicos, HttpStatus.OK);
    }

    // RF23 - Atualizar serviço
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoAtualizado) {
        return servicoRepository.findById(id)
                .map(s -> {
                    s.setNome(servicoAtualizado.getNome());
                    s.setDescricao(servicoAtualizado.getDescricao());
                    s.setPreco(servicoAtualizado.getPreco());
                    Servico servicoSalvo = servicoRepository.save(s);
                    return new ResponseEntity<>(servicoSalvo, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // RF23 - Deletar serviço
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        return servicoRepository.findById(id)
                .map(s -> {
                    servicoRepository.delete(s);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
