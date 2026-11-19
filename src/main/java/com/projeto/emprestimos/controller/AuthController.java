package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.dto.LoginRequest;
import com.projeto.emprestimos.dto.TokenResposta;
import com.projeto.emprestimos.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public TokenResposta login(@RequestBody @Valid LoginRequest dados) {
        Authentication autenticacao = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.usuario(), dados.senha()));
        String perfil = autenticacao.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", ""); // "ROLE_ADMIN" vira "ADMIN"
        return new TokenResposta(tokenService.gerar(autenticacao.getName(), perfil));
    }
}
