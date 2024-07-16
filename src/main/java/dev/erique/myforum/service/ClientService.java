package dev.erique.myforum.service;

import dev.erique.myforum.enums.Role;
import dev.erique.myforum.infra.handler.ResponseFactory;
import dev.erique.myforum.infra.utilities.ErrorSelectorClient;
import dev.erique.myforum.model.user.Client;
import dev.erique.myforum.model.user.ClientBasic;
import dev.erique.myforum.model.user.ClientDTO;
import dev.erique.myforum.model.user.ClientRequest;
import dev.erique.myforum.repository.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ErrorSelectorClient errorSelectorClient;

    public ClientService(ClientRepository clientRepository, ErrorSelectorClient errorSelectorClient) {
        this.clientRepository = clientRepository;
        this.errorSelectorClient = errorSelectorClient;
    }

    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public Object save(ClientRequest clientRequest) {
        Object exceptionClient = errorSelectorClient.validateField(clientRequest);
        if (exceptionClient != null){
            return exceptionClient;
        }

        Client client = new Client();
        BeanUtils.copyProperties(clientRequest, client);
        client.setCreationDate(LocalDateTime.now());
        client.setPassword(passwordEncoder().encode(clientRequest.password()));
        client.setRole(Role.CLIENT);

        clientRepository.save(client);

        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);

        return ResponseFactory.create(clientDTO,"salvo com sucesso","");
    }

    public Object getOne(Integer clientId) {

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null){
            ClientBasic clientBasic = new ClientBasic();

            BeanUtils.copyProperties(client, clientBasic);

            return ResponseFactory.ok(clientBasic,"consulta realizada com sucesso");
        }
        return ResponseFactory.errorNotFound("entidade não encontrada","revise os dados");
    }

    public Object deleteOne(Integer clientId) {

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null){
            clientRepository.deleteById(clientId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT);
        }
        return ResponseFactory.errorNotFound("entidade não encontrada","revise os dados");
    }

    public Object getAll(int page) {
        int size = 20;
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseFactory.ok(clientRepository.findAllClient(pageable),"consulta realizada com sucesso");
    }
}
