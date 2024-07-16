package dev.erique.myforum.service;

import dev.erique.myforum.infra.security.Login;
import dev.erique.myforum.infra.security.LoginRespondeDTO;
import dev.erique.myforum.infra.security.jwt.TokenService;
import dev.erique.myforum.model.admin.Admin;
import dev.erique.myforum.model.user.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginService(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Object adminLogin(Login login) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);
            Object principal = auth.getPrincipal();

            if (principal instanceof Admin admin) {
                var token = tokenService.generateToken(admin);
                return ResponseEntity.ok(new LoginRespondeDTO(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("As credências não pertencem a um administrador");
            }
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Administrador inexistente ou senha inválida");
        }
    }


    public Object clientLogin(Login login) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);
            Object principal = auth.getPrincipal();

            if (principal instanceof Client client) {
                var token = tokenService.generateToken(client);
                return ResponseEntity.ok(new LoginRespondeDTO(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("As credências não pertencem a um cliente");
            }
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Cliente inexistente ou senha inválida");
        }
    }


}
