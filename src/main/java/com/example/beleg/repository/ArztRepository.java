package com.example.beleg.repository;

import com.example.beleg.model.Arzt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository-Interface fuer den Datenbankzugriff auf Aerzte.
 * Erbt grundlegende CRUD-Operationen von JpaRepository.
 */
public interface ArztRepository extends JpaRepository<Arzt, Long> {

    /**
     * Prueft ob ein Arzt mit gleichem Namen und gleicher Fachrichtung bereits existiert.
     * Wird zur Duplikatpruefung beim Erstellen eines neuen Arztes verwendet.
     *
     * @param name         Name des Arztes
     * @param fachrichtung Fachrichtung des Arztes
     * @return true wenn bereits ein Arzt mit diesen Daten existiert, sonst false
     */
    boolean existsByNameAndFachrichtung(String name, String fachrichtung);
}