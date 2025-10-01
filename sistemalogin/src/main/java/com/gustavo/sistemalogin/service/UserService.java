package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.UserCreateDTO;
import com.gustavo.sistemalogin.model.Role;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import com.gustavo.sistemalogin.repository.RoleRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.repository.UserServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserService implements UserServiceRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(UserCreateDTO userCreateDTO) {

        PerfilUsuario perfilEnum = userCreateDTO.getPerfil();

        // Esta linha FALHA porque perfilEnum é null
        Role userRole = roleRepository.findByNome(perfilEnum.name())
                .orElseThrow(() -> new RuntimeException("Erro: Perfil não encontrado no banco de dados."));

        // 3. Cria a nova entidade User
        User newUser = new User();
        newUser.setNome(userCreateDTO.getNome());
        newUser.setEmail(userCreateDTO.getEmail());
        newUser.setSenha(passwordEncoder.encode(userCreateDTO.getSenha())); // Lembre-se de codificar a senha
        newUser.setActive(userCreateDTO.getAtivo());

        // 4. Associa a entidade 'Role' encontrada ao novo usuário.
        newUser.setPerfil(Math.toIntExact(userRole.getId()));


        return userRepository.save(newUser);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Busca o usuário no banco de dados pelo email.
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));

        // 2. Cria uma lista de "autoridades" (perfis/roles) para o usuário.
        //    O Spring Security precisa que os perfis comecem com "ROLE_".
        //    Aqui, estamos a criar um perfil genérico. Pode ser melhorado para ler o `user.getPerfilUsuario()`.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // 3. Retorna um novo objeto User (do Spring Security) com os dados encontrados.
        //    O Spring Security usará este objeto para comparar a senha fornecida no login
        //    com a senha criptografada que está no banco.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getSenha(),
                Collections.singletonList(authority)
        );
    }
}