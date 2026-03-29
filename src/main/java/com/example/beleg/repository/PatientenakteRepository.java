package com.example.beleg.repository;

import com.example.beleg.model.Patientenakte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository-Interface fuer den Datenbankzugriff auf Patientenakten.
 * Erbt grundlegende CRUD-Operationen von JpaRepository.
 */
public interface PatientenakteRepository extends JpaRepository<Patientenakte, Long> {

    /**
     * Sucht die Patientenakte anhand der Patienten-ID.
     * Gibt Optional zurueck da nicht jeder Patient bereits eine Akte haben muss.
     *
     * @param patientId ID des Patienten
     * @return Optional mit der Patientenakte oder leer wenn noch keine Akte existiert
     */
    Optional<Patientenakte> findByPatientId(Long patientId);
}