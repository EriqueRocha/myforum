package dev.erique.myforum.service;

import dev.erique.myforum.repository.AdminRepository;
import dev.erique.myforum.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails admin = adminRepository.findByEmail(username);
        if (admin != null) {
            return admin;
        }

        UserDetails client = clientRepository.findByEmail(username);
        if (client != null) {
            return client;
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }

}