package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service // Anotação importante para que o Spring reconheça este serviço
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                user.isAtivo(),     // 1. enabled (aqui está a sua verificação!)
                true,               // 2. accountNonExpired
                true,               // 3. credentialsNonExpired
                true,               // 4. accountNonLocked
                authorities
        );
    }
}