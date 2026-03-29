package com.example.beleg.repository;

import com.example.beleg.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository-Interface fuer den Datenbankzugriff auf Termine.
 * Erbt grundlegende CRUD-Operationen von JpaRepository und bietet
 * zusaetzliche Suchmethoden fuer Filter- und Statistikfunktionen.
 */
public interface TerminRepository extends JpaRepository<Termin, Long> {

    /**
     * Sucht einen Termin anhand von Arzt, Datum und Uhrzeit.
     * Wird zur Pruefung auf Doppelbuchungen verwendet.
     *
     * @param arztId  ID des Arztes
     * @param datum   Datum des Termins
     * @param uhrzeit Uhrzeit des Termins
     * @return Optional mit dem gefundenen Termin oder leer wenn frei
     */
    Optional<Termin> findByArztIdAndDatumAndUhrzeit(Long arztId, LocalDate datum, LocalTime uhrzeit);

    /**
     * Gibt alle Termine fuer ein bestimmtes Datum zurueck.
     *
     * @param datum Das gesuchte Datum
     * @return Liste der Termine an diesem Datum
     */
    List<Termin> findByDatum(LocalDate datum);

    /**
     * Sucht Termine anhand eines Teilstrings im Patientennamen (Gross-/Kleinschreibung ignoriert).
     *
     * @param name Suchbegriff fuer den Patientennamen
     * @return Liste der gefundenen Termine
     */
    List<Termin> findByPatientNameContainingIgnoreCase(String name);

    /**
     * Loescht alle Termine eines bestimmten Patienten anhand der Patienten-ID.
     * Wird vor dem Loeschen eines Patienten aufgerufen, um referenzielle
     * Integritaet zu gewaehrleisten.
     *
     * @param patientId ID des Patienten, dessen Termine geloescht werden sollen
     */
    @Transactional
    void deleteByPatientId(Long patientId);

    /**
     * Sucht Termine anhand eines Teilstrings im Arztnamen (Gross-/Kleinschreibung ignoriert).
     *
     * @param name Suchbegriff fuer den Arztnamen
     * @return Liste der gefundenen Termine
     */
    List<Termin> findByArztNameContainingIgnoreCase(String name);

    /**
     * Gibt alle Termine mit einem bestimmten Status zurueck.
     *
     * @param status Der gesuchte Terminstatus
     * @return Liste der Termine mit diesem Status
     */
    List<Termin> findByStatus(Termin.Status status);

    /**
     * Gibt alle Termine in einem bestimmten Zeitraum zurueck, sortiert nach Datum und Uhrzeit.
     *
     * @param von Startdatum des Zeitraums (inklusive)
     * @param bis Enddatum des Zeitraums (inklusive)
     * @return Liste der Termine im Zeitraum, sortiert nach Datum und Uhrzeit
     */
    @Query("SELECT t FROM Termin t WHERE t.datum BETWEEN :von AND :bis ORDER BY t.datum, t.uhrzeit")
    List<Termin> findByDatumBetween(@Param("von") LocalDate von, @Param("bis") LocalDate bis);

    /**
     * Gibt eine Statistik der Terminanzahl pro Arzt zurueck, absteigend sortiert.
     * Wird fuer das Dashboard zur Auslastungsanzeige verwendet.
     *
     * @return Liste von Object-Arrays mit [Arztname, Fachrichtung, Anzahl Termine]
     */
    @Query("SELECT t.arzt.name, t.arzt.fachrichtung, COUNT(t) FROM Termin t GROUP BY t.arzt.id, t.arzt.name, t.arzt.fachrichtung ORDER BY COUNT(t) DESC")
    List<Object[]> countTermineByArzt();
}