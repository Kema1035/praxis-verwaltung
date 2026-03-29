package com.example.beleg.repository;

import com.example.beleg.model.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository-Interface fuer den Datenbankzugriff auf Diagnosen.
 * Erbt grundlegende CRUD-Operationen von JpaRepository.
 */
public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {

    /**
     * Gibt alle Diagnosen eines Patienten sortiert nach Datum zurueck.
     * Neueste Diagnosen erscheinen zuerst.
     *
     * @param patientId ID des Patienten
     * @return Liste der Diagnosen, absteigend sortiert nach Datum
     */
    List<Diagnose> findByPatientIdOrderByDatumDesc(Long patientId);
}