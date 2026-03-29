package com.example.beleg.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity-Klasse fuer einen Termin in der Arztpraxis.
 * Verbindet einen Patienten mit einem Arzt zu einem bestimmten Datum und Uhrzeit.
 * Enthaelt Status- und Typenklassifikation des Termins.
 */
@Entity
public class Termin {

    /** Eindeutige ID des Termins (automatisch generiert) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Patient dem dieser Termin zugeordnet ist (Pflichtfeld) */
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** Arzt der den Termin durchfuehrt (Pflichtfeld) */
    @ManyToOne
    @JoinColumn(name = "arzt_id", nullable = false)
    private Arzt arzt;

    /** Datum des Termins */
    private LocalDate datum;

    /** Uhrzeit des Termins */
    private LocalTime uhrzeit;

    /** Aktueller Status des Termins, Standardwert: GEPLANT */
    @Enumerated(EnumType.STRING)
    private Status status = Status.GEPLANT;

    /** Art des Termins, Standardwert: ERSTUNTERSUCHUNG */
    @Enumerated(EnumType.STRING)
    private TerminArt terminArt = TerminArt.ERSTUNTERSUCHUNG;

    /**
     * Enum fuer den Bearbeitungsstatus eines Termins.
     */
    public enum Status {
        /** Termin ist geplant aber noch nicht bestaetigt */
        GEPLANT,
        /** Termin wurde bestaetigt */
        BESTAETIGT,
        /** Termin wurde abgesagt */
        ABGESAGT
    }

    /**
     * Enum fuer die Art eines Termins.
     */
    public enum TerminArt {
        /** Erste Untersuchung eines neuen Patienten */
        ERSTUNTERSUCHUNG,
        /** Wiederholungstermin zur Kontrolle */
        KONTROLLTERMIN,
        /** Dringender Notfalltermin */
        NOTFALL,
        /** Beratungsgespraech ohne Untersuchung */
        BERATUNG,
        /** Regelmaessige Vorsorgeuntersuchung */
        VORSORGE,
        /** Operativer Eingriff */
        OPERATION
    }

    /** Getters und Setters*/
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Arzt getArzt() { return arzt; }
    public void setArzt(Arzt arzt) { this.arzt = arzt; }
    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }
    public LocalTime getUhrzeit() { return uhrzeit; }
    public void setUhrzeit(LocalTime uhrzeit) { this.uhrzeit = uhrzeit; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public TerminArt getTerminArt() { return terminArt; }
    public void setTerminArt(TerminArt terminArt) { this.terminArt = terminArt; }
}