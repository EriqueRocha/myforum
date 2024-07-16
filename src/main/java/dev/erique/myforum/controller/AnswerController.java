package dev.erique.myforum.controller;

import dev.erique.myforum.model.answers.AnswerRequest;
import dev.erique.myforum.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/saveNew{topicId}")
    @Operation(summary = "cadastrar uma resposta (client)")
    public Object save(@RequestHeader("Authorization") String token, @RequestBody AnswerRequest answerRequest, @PathVariable Integer topicId) {
        return answerService.save(token, answerRequest, topicId);
    }

    @GetMapping("/getOne/{answerId}")
    @Operation(summary = "retorna uma resposta especifica (all)")
    public Object getOne(@PathVariable Integer answerId) {
        return answerService.getOne(answerId);
    }

    @GetMapping("/getAll/{page}")
    @Operation(summary = "retorna a lista de respostas de um usuário (client)")
    public Object getAll(@RequestHeader("Authorization") String token, @PathVariable int page) {
        return answerService.getAll(token, page);
    }

    @DeleteMapping("/deleteOne/{answerId}")
    @Operation(summary = "deleta um tópico específico (client)")
    public Object deleteOne(@RequestHeader("Authorization") String token, @PathVariable Integer answerId) {
        return answerService.deleteOne(token, answerId);
    }

}
