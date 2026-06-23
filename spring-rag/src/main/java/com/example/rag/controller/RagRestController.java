package com.example.rag.controller;

import com.example.rag.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RagRestController {

    private final RagService ragService;

    public RagRestController(
            RagService ragService) {

        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public String ask(
            @RequestParam String question) {

        return ragService.ask(question);
    }
}