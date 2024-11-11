package com.magasinpeche.repository;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Concours;
import com.magasinpeche.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    // Rechercher les participations d'un client (sans utiliser l'email)
    List<Participation> findByClient(Client client);

    // Rechercher les participations d'un client (avec l'email)
    Participation findByConcoursAndClient(Concours concours, Client client);
}
