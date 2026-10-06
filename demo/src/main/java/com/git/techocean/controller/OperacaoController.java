package com.git.techocean.controller;

import com.git.techocean.model.Operacao;
import com.git.techocean.repository.OperacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operacoes")
public class OperacaoController {

    private final OperacaoRepository operacaoRepository;

    public OperacaoController(OperacaoRepository operacaoRepository) {
        this.operacaoRepository = operacaoRepository;
    }

    // RF10 - Operação de Importação
    @PostMapping("/importacao")
    public ResponseEntity<Operacao> cadastrarImportacao(@RequestBody Operacao operacao) {
        operacao.setTipo("IMPORTACAO");
        operacao.setStatus("EM_ANDAMENTO");
        return ResponseEntity.status(HttpStatus.CREATED).body(operacaoRepository.save(operacao));
    }

    // RF11 - Operação de Exportação
    @PostMapping("/exportacao")
    public ResponseEntity<Operacao> cadastrarExportacao(@RequestBody Operacao operacao) {
        operacao.setTipo("EXPORTACAO");
        operacao.setStatus("EM_ANDAMENTO");
        return ResponseEntity.status(HttpStatus.CREATED).body(operacaoRepository.save(operacao));
    }

    // RF15 - Alterar status de uma operação
    @PatchMapping("/{id}/status")
    public ResponseEntity<Operacao> alterarStatus(@PathVariable Long id, @RequestParam String status) {
        return operacaoRepository.findById(id).map(op -> {
            op.setStatus(status);
            return ResponseEntity.ok(operacaoRepository.save(op));
        }).orElse(ResponseEntity.notFound().build());
    }

    // RF17 / RF27 - Listar/Acompanhar operações
    @GetMapping
    public ResponseEntity<List<Operacao>> listarTodas() {
        return ResponseEntity.ok(operacaoRepository.findAll());
    }
}