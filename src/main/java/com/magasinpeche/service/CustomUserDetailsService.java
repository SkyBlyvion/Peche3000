package com.magasinpeche.service;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Role;
import com.magasinpeche.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        // Crée un UserDetails avec le mot de passe et les informations de l'utilisateur
        return User.withUsername(client.getEmail())
                .password(client.getPassword())
                .authorities(getAuthorities(client))  // Utiliser la méthode pour obtenir les rôles
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Client client) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + client.getRole().name()));
    }
}
