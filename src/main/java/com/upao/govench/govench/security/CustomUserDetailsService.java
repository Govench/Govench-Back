package com.upao.govench.govench.security;

import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.UserRepository;
import kotlin.ReplaceWith;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado con el email : "+email));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority),
                user
        );
    }
}
