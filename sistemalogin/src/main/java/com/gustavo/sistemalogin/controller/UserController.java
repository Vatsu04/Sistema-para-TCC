package com.gustavo.sistemalogin.controller;


import com.gustavo.sistemalogin.dto.UserDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/cadastro")
    public ResponseEntity<User> cadastrar(@RequestBody UserDTO userDTO) {
        User novoUsuario = userService.cadastrarUsuario(userDTO);
        return ResponseEntity.ok(novoUsuario);
    }
}