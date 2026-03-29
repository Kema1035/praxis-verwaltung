package com.example.beleg.service;

import com.example.beleg.model.Arzt;
import com.example.beleg.model.Termin;
import com.example.beleg.repository.TerminRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Service-Klasse fuer die Geschaeftslogik rund um Termine.
 * Kapselt die Validierungslogik und stellt sicher, dass keine
 * Doppelbuchungen fuer denselben Arzt entstehen.
 */
@Service
public class TerminService {

    private final TerminRepository terminRepository;

    /**
     * Konstruktor mit Dependency Injection des Termin-Repositories.
     *
     * @param terminRepository Repository fuer Termin-Datenbankoperationen
     */
    public TerminService(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }

    /**
     * Prueft ob ein Arzt zu einem bestimmten Datum und Uhrzeit verfuegbar ist.
     *
     * @param arzt    Der zu pruefende Arzt
     * @param datum   Das gewuenschte Datum
     * @param uhrzeit Die gewuenschte Uhrzeit
     * @return true wenn der Arzt frei ist, false bei Doppelbuchung
     */
    public boolean istTerminFrei(Arzt arzt, LocalDate datum, LocalTime uhrzeit) {
        return terminRepository.findByArztIdAndDatumAndUhrzeit(arzt.getId(), datum, uhrzeit).isEmpty();
    }

    /**
     * Speichert einen Termin nach erfolgreicher Verfuegbarkeitspruefung.
     * Wirft eine RuntimeException wenn der Arzt zu diesem Zeitpunkt bereits gebucht ist.
     *
     * @param termin Der zu speichernde Termin
     * @throws RuntimeException wenn der Arzt zu diesem Zeitpunkt bereits einen Termin hat
     */
    public void saveTermin(Termin termin) {
        if (!istTerminFrei(termin.getArzt(), termin.getDatum(), termin.getUhrzeit())) {
            throw new RuntimeException("Dieser Arzt ist zu diesem Termin bereits gebucht.");
        }
        terminRepository.save(termin);
    }
}