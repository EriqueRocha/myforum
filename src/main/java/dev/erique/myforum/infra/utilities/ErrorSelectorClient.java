package dev.erique.myforum.infra.utilities;

import dev.erique.myforum.infra.handler.ResponseFactory;
import dev.erique.myforum.model.user.ClientRequest;
import dev.erique.myforum.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorSelectorClient {

    private final ClientRepository clientRepository;

    public ErrorSelectorClient(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Object validateField (ClientRequest clientRequest){
        List<String> field = new ArrayList<>();

        if (clientRequest.email() == null || clientRequest.email().isEmpty()){
            field.add("Email nulo ou vazio");
        }if (clientRequest.name() == null || clientRequest.name().isEmpty()){
            field.add("Nome nulo ou vazio");
        }if (clientRequest.password() == null || clientRequest.password().isEmpty()){
            field.add("Senha nula ou vazia");
        }if (clientRepository.existsByEmail(clientRequest.email())){
            field.add("Email já está em uso");
        }if(!field.isEmpty()){
            return ResponseFactory.errorNotAcceptable( field, "os seguintes campos precisam ser revisados","corrija os campos e tente novamente");
        }
        return null;
    }

}
