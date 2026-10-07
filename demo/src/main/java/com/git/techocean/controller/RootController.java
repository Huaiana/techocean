package com.git.techocean.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
                "name", "Techocean API",
                "status", "online",
                "endpoints", List.of("/clientes", "/agendamentos", "/servicos"));
    }
}
