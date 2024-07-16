package dev.erique.myforum.service;

import dev.erique.myforum.enums.TopicStatus;
import dev.erique.myforum.infra.handler.ResponseFactory;
import dev.erique.myforum.infra.security.jwt.TokenService;
import dev.erique.myforum.model.answers.Answer;
import dev.erique.myforum.model.answers.AnswerDTO;
import dev.erique.myforum.model.answers.AnswerRequest;
import dev.erique.myforum.model.topic.Topic;
import dev.erique.myforum.model.user.Client;
import dev.erique.myforum.repository.AnswerRepository;
import dev.erique.myforum.repository.ClientRepository;
import dev.erique.myforum.repository.TopicRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerService {

    private final ClientRepository clientRepository;
    private final TokenService tokenService;
    private final TopicRepository topicRepository;
    private final AnswerRepository answerRepository;


    public AnswerService(ClientRepository clientRepository, TokenService tokenService, TopicRepository topicRepository, AnswerRepository answerRepository) {
        this.clientRepository = clientRepository;
        this.tokenService = tokenService;
        this.topicRepository = topicRepository;
        this.answerRepository = answerRepository;
    }


    public Object save(String token, AnswerRequest answerRequest, Integer topicId) {
        Client client = clientRepository.findClientByEmail(tokenService.getSubject(token));
        if (client != null){
            Topic topic = topicRepository.findById(topicId).orElse(null);

            if (topic != null){
                List<Answer> answersTopic = topic.getAnswers();
                List<Answer> answersClient = client.getAnswers();
                Answer answer = new Answer();

                BeanUtils.copyProperties(answerRequest, answer);
                answer.setCreationDate(LocalDateTime.now());
                answer.setUserName(client.getName());
                answer.setClient(client);
                answer.setTopic(topic);
                if (topic.getStatus().equals(TopicStatus.NOT_ANSWERED)){
                    topic.setStatus(TopicStatus.ANSWERED);
                }

                answerRepository.save(answer);

                answersTopic.add(answer);
                answersClient.add(answer);

                client.setAnswers(answersClient);
                topic.setAnswers(answersTopic);

                clientRepository.save(client);
                topicRepository.save(topic);

                return ResponseFactory.ok(answer.getCreationDate(), "Resposta salva com sucesso");
            }
            return ResponseFactory.errorNotFound("Não foi encontrada uma entidade de tópico","Reveja o token");

        }

        return ResponseFactory.errorNotFound("Não foi encontrada uma entidade de client","Reveja o token");

    }

    public Object getOne(Integer answerId) {
        Answer answer = answerRepository.findById(answerId).orElse(null);
        if (answer!=null){
            AnswerDTO answerDTO = new AnswerDTO();
            BeanUtils.copyProperties(answer, answerDTO);

            return ResponseFactory.ok(answerDTO,"consilta relizada cm sucesso");
        }
        return ResponseFactory.errorNotFound("Nenhuma resposta encontrada","revise os dados");
    }

    public Object getAll(String token, int page) {
        Client client = clientRepository.findClientByEmail(tokenService.getSubject(token));
        if (client != null){
            int size = 20;
            PageRequest pageable = PageRequest.of(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(answerRepository.findAllAnswer(client, pageable));
        }
        return ResponseFactory.errorNotFound("erro no token","revise os dados");
    }

    public Object deleteOne(String token, Integer answerId) {
        Client client = clientRepository.findClientByEmail(tokenService.getSubject(token));
        Answer answer = answerRepository.findAnswer(client, answerId);
        if (answer!=null){
            answerRepository.deleteById(answerId);
            return ResponseEntity.noContent().build();
        }
        return ResponseFactory.errorNotFound("Nenhuma resposta encontrada","revise os dados");
    }
}
