package com.magasinpeche.repository;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Commande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);
    List<Commande> findTop3ByClientIdOrderByDateCommandeDesc(Long clientId);
}
