package dev.erique.myforum.controller;

import dev.erique.myforum.model.topic.TopicRequest;
import dev.erique.myforum.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/saveNew")
    @Operation(summary = "cadastrar um tópico (client)")
    public Object save(@RequestHeader("Authorization") String token, @RequestBody TopicRequest topicRequest) {
        return topicService.save(token, topicRequest);
    }

    @GetMapping("/getOne/{topicId}")
    @Operation(summary = "retorna um tópico específico (all)")
    public Object getOne(@PathVariable Integer topicId) {
        return topicService.getOne(topicId);
    }

    @GetMapping("/getAll/{page}")
    @Operation(summary = "retorna a lista de tópicos (all)")
    public Object getAll(@PathVariable int page) {
        return topicService.getAll(page);
    }

    @GetMapping("/getAll/{topicId}/{page}")
    @Operation(summary = "retorna a lista de resposta de um tópico (all)")
    public Object getAll(@PathVariable int page, @PathVariable Integer topicId) {
        return topicService.getAllAnswer(topicId, page);
    }

    @DeleteMapping("/deleteOne/{topictId}")
    @Operation(summary = "deleta um tópico específico (client)")
    public Object deleteOne(@RequestHeader("Authorization") String token, @PathVariable Integer topictId) {
        return topicService.deleteOne(token, topictId);
    }

}
