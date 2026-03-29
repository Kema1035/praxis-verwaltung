package com.example.beleg.config;

import com.example.beleg.model.Arzt;
import com.example.beleg.model.Patient;
import com.example.beleg.repository.ArztRepository;
import com.example.beleg.repository.PatientRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Initialisierungsklasse fuer Testdaten der Arztpraxis-Anwendung.
 * Wird automatisch nach dem Start der Anwendung ausgefuehrt und
 * fuellt die Datenbank mit Beispieldaten, sofern diese noch leer ist.
 */
@Component
public class DataInitializer {

    private final ArztRepository arztRepository;
    private final PatientRepository patientRepository;

    /**
     * Konstruktor mit Dependency Injection der benoedigten Repositories.
     *
     * @param arztRepository    Repository fuer Arzt-Datenbankoperationen
     * @param patientRepository Repository fuer Patienten-Datenbankoperationen
     */
    public DataInitializer(ArztRepository arztRepository,
                           PatientRepository patientRepository) {
        this.arztRepository = arztRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Einstiegspunkt der Initialisierung.
     * Wird nach dem vollstaendigen Start der Anwendung aufgerufen.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        initAerzte();
        initPatienten();
    }

    /**
     * Legt Beispielärzte an, falls noch keine vorhanden sind.
     */
    private void initAerzte() {
        if (arztRepository.count() > 0) return;

        arztRepository.save(arzt("Dr. Anna Müller",       "Allgemeinmedizin"));
        arztRepository.save(arzt("Dr. Thomas Schmidt",    "Kardiologie"));
        arztRepository.save(arzt("Dr. Maria Fischer",     "Neurologie"));
        arztRepository.save(arzt("Dr. Klaus Weber",       "Orthopädie"));
        arztRepository.save(arzt("Dr. Sandra Becker",     "Pädiatrie"));
        arztRepository.save(arzt("Dr. Michael Wagner",    "Dermatologie"));
        arztRepository.save(arzt("Dr. Laura Hoffmann",    "Gynäkologie"));
    }

    /**
     * Legt Beispielpatienten an, falls noch keine vorhanden sind.
     */
    private void initPatienten() {

        if (patientRepository.count() > 0) return;

        patientRepository.save(patient("Max Mustermann",   LocalDate.of(1985, 3, 15), "+49 151 12345678"));
        patientRepository.save(patient("Erika Musterfrau", LocalDate.of(1990, 7, 22), "+49 152 87654321"));
        patientRepository.save(patient("Hans Meier",       LocalDate.of(1975, 11, 5), "+49 153 11223344"));
        patientRepository.save(patient("Petra Schulz",     LocalDate.of(2000, 1, 30), "+49 154 55667788"));
        patientRepository.save(patient("Otto Braun",       LocalDate.of(1960, 6, 18), "+49 155 99887766"));
    }

    /**
     * Hilfsmethode zum Erstellen eines Arzt-Objekts.
     *
     * @param name         Name des Arztes
     * @param fachrichtung Fachrichtung des Arztes
     * @return Neues Arzt-Objekt
     */
    private Arzt arzt(String name, String fachrichtung) {
        Arzt a = new Arzt();
        a.setName(name);
        a.setFachrichtung(fachrichtung);
        return a;
    }

    /**
     * Hilfsmethode zum Erstellen eines Patienten-Objekts.
     *
     * @param name         Name des Patienten
     * @param geburtsdatum Geburtsdatum des Patienten
     * @param telefon      Telefonnummer des Patienten
     * @return Neues Patient-Objekt
     */
    private Patient patient(String name, LocalDate geburtsdatum, String telefon) {
        Patient p = new Patient();
        p.setName(name);
        p.setGeburtsdatum(geburtsdatum);
        p.setTelefon(telefon);
        return p;
    }
}