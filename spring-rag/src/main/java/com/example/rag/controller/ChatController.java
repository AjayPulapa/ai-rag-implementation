
package com.example.rag.controller;

import com.example.rag.service.RagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final RagService ragService;

    public ChatController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/ask")
    public String ask(
            @RequestParam String question,
            Model model) {

        String answer =
                ragService.ask(question);

        model.addAttribute("question", question);
        model.addAttribute("answer", answer);

        return "index";
    }
}