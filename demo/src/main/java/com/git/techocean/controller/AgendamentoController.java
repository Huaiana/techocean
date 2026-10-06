package com.git.techocean.controller;

import com.git.techocean.model.Agendamento;
import com.git.techocean.service.AgendamentoException;
import com.git.techocean.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<Agendamento> solicitarVisita(@RequestBody Agendamento request) {
        Agendamento agendamento = agendamentoService.solicitarVisita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
    }

    @ExceptionHandler(AgendamentoException.class)
    public ResponseEntity<ApiError> handleRequestError(AgendamentoException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(new ApiError(exception.getCode(), exception.getMessage()));
    }

    public record ApiError(String code, String message) {
    }
}
