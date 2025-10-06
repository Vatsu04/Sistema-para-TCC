package com.gustavo.sistemalogin.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserServiceRepository {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
