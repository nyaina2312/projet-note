package com.exemple.projet.repository;

import com.exemple.projet.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {
    
    @Query("SELECT d FROM Demande d JOIN FETCH d.client")
    List<Demande> findAllWithClient();
    
    @Query("SELECT d FROM Demande d JOIN FETCH d.client WHERE d.id = :id")
    Optional<Demande> findByIdWithClient(@Param("id") Integer id);
    
}
