package dev.erique.myforum.infra.security.jwt;

import dev.erique.myforum.repository.AdminRepository;
import dev.erique.myforum.repository.ClientRepository;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;

    public SecurityFilter(TokenService tokenService, AdminRepository adminRepository, ClientRepository clientRepository) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var login = tokenService.validadeToken(token);
            UserDetails user = findUserByEmail(login);

            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private UserDetails findUserByEmail(String email) {
        UserDetails user = adminRepository.findByEmail(email);
        if (user == null) {
            user = clientRepository.findByEmail(email);
        }
        return user;
    }

}