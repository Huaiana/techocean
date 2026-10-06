package com.git.porto.controller;

import com.git.porto.model.Orcamento;
import com.git.porto.repository.OrcamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoController(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }
    
    // RF07 - Solicitar orçamento
    @PostMapping("/solicitar")
    public ResponseEntity<Orcamento> solicitarOrcamento(@RequestBody Orcamento orcamento) {
        orcamento.setStatus("pendente");
        Orcamento novo = orcamentoRepository.save(orcamento);
        return new ResponseEntity<>(novo, HttpStatus.CREATED);
    }

    // RF16 - Cliente consultar os seus orçamentos
    @GetMapping("/consultar/{clienteId}")
    public ResponseEntity<List<Orcamento>> consultarOrcamentos(@PathVariable Long clienteId) {
        List<Orcamento> orcamentos = orcamentoRepository.findByClienteId(clienteId);
        return new ResponseEntity<>(orcamentos, HttpStatus.OK);
    }

    // RF14 - Alterar status do orçamento
    @PutMapping("/{id}/status")
    public ResponseEntity<Orcamento> alterarStatusOrcamento(@PathVariable Long id, @RequestParam String status) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado com o ID: " + id));
        orcamento.setStatus(status);
        Orcamento atualizado = orcamentoRepository.save(orcamento);
        return new ResponseEntity<>(atualizado, HttpStatus.OK);
    }
}
   