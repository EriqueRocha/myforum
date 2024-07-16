package dev.erique.myforum.controller;

import dev.erique.myforum.infra.security.Login;
import dev.erique.myforum.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class LoginResource {

    private final LoginService loginService;

    public LoginResource(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/loginAdmin")
    @Operation(summary = "login admin")
    public Object adminLogin(@RequestBody @Valid Login login){
        return loginService.adminLogin(login);
    }

    @PostMapping("/loginClient")
    @Operation(summary = "login cliente")
    public Object clientLogin(@RequestBody Login login){
        return loginService.clientLogin(login);
    }

}
