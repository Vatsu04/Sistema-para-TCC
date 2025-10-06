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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo utilizador com um perfil padrão.
     */
    public User createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        User newUser = new User();
        newUser.setNome(userCreateDTO.getNome());
        newUser.setEmail(userCreateDTO.getEmail());
        newUser.setSenha(passwordEncoder.encode(userCreateDTO.getSenha()));
        newUser.setAtivo(true); // Define o utilizador como ativo por defeito

        // --- LÓGICA CORRIGIDA ---
        // Atribui o perfil padrão (ASSISTENTE) diretamente no servidor.
        newUser.setPerfil(PerfilUsuario.ASSISTENTE);

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