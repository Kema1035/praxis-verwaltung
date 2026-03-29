package com.example.beleg.repository;

import com.example.beleg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository-Interface fuer den Datenbankzugriff auf Patienten.
 * Erbt grundlegende CRUD-Operationen von JpaRepository.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Sucht Patienten anhand eines Teilstrings im Namen (Gross-/Kleinschreibung ignoriert).
     *
     * @param name Suchbegriff fuer den Patientennamen
     * @return Liste der gefundenen Patienten
     */
    List<Patient> findByNameContainingIgnoreCase(String name);

    /**
     * Prueft ob ein Patient mit gleichem Namen und gleichem Geburtsdatum bereits existiert.
     * Wird zur Duplikatpruefung beim Erstellen eines neuen Patienten verwendet.
     *
     * @param name         Name des Patienten
     * @param geburtsdatum Geburtsdatum des Patienten
     * @return true wenn bereits ein Patient mit diesen Daten existiert, sonst false
     */
    boolean existsByNameAndGeburtsdatum(String name, LocalDate geburtsdatum);
}