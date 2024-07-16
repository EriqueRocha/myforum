package dev.erique.myforum.controller;

import dev.erique.myforum.model.user.ClientRequest;
import dev.erique.myforum.repository.ClientRepository;
import dev.erique.myforum.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ClientController(ClientService clientService,
                            ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/saveNew")
    @Operation(summary = "cadastrar um User (all)")
    public Object save(@RequestBody ClientRequest clientRequest) {
        return clientService.save(clientRequest);
    }

    @GetMapping("/getOne/{clientId}")
    @Operation(summary = "retorna um usuário específico (admin)")
    public Object getOne(@PathVariable Integer clientId) {
        return clientService.getOne(clientId);
    }

    @GetMapping("/getAll/{page}")
    @Operation(summary = "retorna a lista de usuários (admin)")
    public Object getAll(@PathVariable int page) {
        return clientService.getAll(page);
    }

    @DeleteMapping("/deleteOne/{clientId}")
    @Operation(summary = "deleta um usuário especifico (admin)")
    public Object deleteOne(@PathVariable Integer clientId) {
        return clientService.deleteOne(clientId);
    }

//----------------------  T E S T E -----------------------------------

    @GetMapping("/teste")
    @Operation(summary = "teste (all)")
    public Object teste() {
        return clientRepository.findAll();
    }


}
