package dev.erique.myforum.service;

import dev.erique.myforum.enums.TopicStatus;
import dev.erique.myforum.infra.handler.ResponseFactory;
import dev.erique.myforum.infra.security.jwt.TokenService;
import dev.erique.myforum.model.topic.Topic;
import dev.erique.myforum.model.topic.TopicDTO;
import dev.erique.myforum.model.topic.TopicDTOMessage;
import dev.erique.myforum.model.topic.TopicRequest;
import dev.erique.myforum.model.user.Client;
import dev.erique.myforum.repository.AnswerRepository;
import dev.erique.myforum.repository.ClientRepository;
import dev.erique.myforum.repository.TopicRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final ClientRepository clientRepository;
    private final TokenService tokenService;
    private final AnswerRepository answerRepository;

    public TopicService(TopicRepository topicRepository, ClientRepository clientRepository, TokenService tokenService, AnswerRepository answerRepository) {
        this.topicRepository = topicRepository;
        this.clientRepository = clientRepository;
        this.tokenService = tokenService;
        this.answerRepository = answerRepository;
    }

    public Object save(String token, TopicRequest topicRequest) {
        Client client = clientRepository.findClientByEmail(tokenService.getSubject(token));

        if (client != null){
            List<Topic> topics = client.getTopics();
            Topic topic = new Topic();

            BeanUtils.copyProperties(topicRequest, topic);
            topic.setCreationDate(LocalDateTime.now());
            topic.setUserName(client.getName());
            topic.setStatus(TopicStatus.NOT_ANSWERED);
            topics.add(topic);
            topic.setClient(client);

            topicRepository.save(topic);
            client.setTopics(topics);
            clientRepository.save(client);
            TopicDTO topicDTO = new TopicDTO();

            BeanUtils.copyProperties(topic, topicDTO);

            return ResponseFactory.ok(topicDTO, "Tópico salvo com sucesso");
        }

        return ResponseFactory.errorNotFound("Não foi encontrada uma entidade de client","Reveja o token");

    }

    public Object getOne(Integer topicId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (topic !=null){
            TopicDTOMessage topicDTOMessage = new TopicDTOMessage();
            BeanUtils.copyProperties(topic, topicDTOMessage);
            return ResponseFactory.ok(topicDTOMessage,"Consulta realizada com sucesso");
        }
        return ResponseFactory.errorNotFound("Nenhum tópico encontrado","revise os dados");
    }

    public Object getAll(int page) {
        int size = 20;
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(topicRepository.findAllTopic(pageable));
    }

    @Transactional
    public Object deleteOne(String token, Integer topicId) {
        Client client = clientRepository.findClientByEmail(tokenService.getSubject(token));
        if (client != null) {
            topicRepository.deleteById(topicId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseFactory.errorNotFound("entidade não encontrada", "revise os dados");
    }


    public Object getAllAnswer(Integer topicId, int page) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (topic != null){
            int size = 20;
            PageRequest pageable = PageRequest.of(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(answerRepository.findAllAnswerForTopic(topic, pageable));
        }
        return ResponseFactory.errorNotFound("Tópico não encontrado","revise os dados");
    }
}
