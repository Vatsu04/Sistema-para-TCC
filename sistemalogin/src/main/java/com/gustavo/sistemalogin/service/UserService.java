package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.UserCreateDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção de dependências via construtor (boa prática)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserCreateDTO userCreateDTO) {
        // Validação de segurança: verifica se o e-mail já existe
        if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        User newUser = new User();
        newUser.setNome(userCreateDTO.getNome());
        newUser.setEmail(userCreateDTO.getEmail());
        newUser.setSenha(passwordEncoder.encode(userCreateDTO.getSenha())); // Sempre codificar a senha!

        // --- LÓGICA DE SEGURANÇA E CORREÇÃO DO ERRO ---
        // 1. O servidor define o usuário como ativo por padrão.
        newUser.setAtivo(true);
        // 2. O servidor atribui um perfil padrão. O cliente nunca deve decidir isso.
        newUser.setPerfil(PerfilUsuario.ASSISTENTE);
        // ------------------------------------------------

        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado com o email: " + username));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getPerfil() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getPerfil().getDescricao()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getSenha(),
                authorities
        );
    }
}