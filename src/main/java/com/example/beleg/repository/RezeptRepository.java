package com.example.beleg.repository;

import com.example.beleg.model.Rezept;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository-Interface fuer den Datenbankzugriff auf Rezepte.
 * Erbt grundlegende CRUD-Operationen von JpaRepository.
 */
public interface RezeptRepository extends JpaRepository<Rezept, Long> {

    /**
     * Gibt alle Rezepte eines Patienten sortiert nach Ausstellungsdatum zurueck.
     * Neueste Rezepte erscheinen zuerst.
     *
     * @param patientId ID des Patienten
     * @return Liste der Rezepte, absteigend sortiert nach Ausstellungsdatum
     */
    List<Rezept> findByPatientIdOrderByAusstellungsdatumDesc(Long patientId);

    /**
     * Gibt alle Rezepte zurueck die von einem bestimmten Arzt ausgestellt wurden.
     *
     * @param arztId ID des Arztes
     * @return Liste der Rezepte des Arztes
     */
    List<Rezept> findByArztId(Long arztId);
}