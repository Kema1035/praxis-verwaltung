package com.example.beleg.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity-Klasse fuer ein Rezept in der Arztpraxis.
 * Ein Rezept wird von einem Arzt fuer einen Patienten ausgestellt
 * und enthaelt Informationen ueber Medikament, Dosierung und Gueltigkeitsdauer.
 */
@Entity
public class Rezept {

    /** Eindeutige ID des Rezepts (automatisch generiert) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Patient fuer den das Rezept ausgestellt wird (Pflichtfeld) */
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** Arzt der das Rezept ausstellt (Pflichtfeld) */
    @ManyToOne
    @JoinColumn(name = "arzt_id", nullable = false)
    private Arzt arzt;

    /** Termin bei dem das Rezept ausgestellt wurde (optional) */
    @ManyToOne
    @JoinColumn(name = "termin_id")
    private Termin termin;

    /** Название лекарства - Name des verschriebenen Medikaments */
    private String medikament;

    /** Дозировка - Dosierungsangabe des Medikaments */
    private String dosierung;

    /** Указания по приёму - Hinweise zur Einnahme des Medikaments */
    private String einnahmehinweis;

    /** Дата выписки - Datum an dem das Rezept ausgestellt wurde */
    private LocalDate ausstellungsdatum;

    /** Действует до - Datum bis zu dem das Rezept gueltig ist */
    private LocalDate gueltigBis;

    /** Aktueller Status des Rezepts, Standardwert: AUSGESTELLT */
    @Enumerated(EnumType.STRING)
    private RezeptStatus status = RezeptStatus.AUSGESTELLT;

    /**
     * Enum fuer den Status eines Rezepts.
     */
    public enum RezeptStatus {
        /** Rezept wurde ausgestellt aber noch nicht eingeloest */
        AUSGESTELLT,
        /** Rezept wurde in der Apotheke eingeloest */
        EINGELOEST,
        /** Rezept ist abgelaufen und nicht mehr gueltig */
        ABGELAUFEN
    }

    /**Getters und Setters*/
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Arzt getArzt() { return arzt; }
    public void setArzt(Arzt arzt) { this.arzt = arzt; }
    public Termin getTermin() { return termin; }
    public void setTermin(Termin termin) { this.termin = termin; }
    public String getMedikament() { return medikament; }
    public void setMedikament(String medikament) { this.medikament = medikament; }
    public String getDosierung() { return dosierung; }
    public void setDosierung(String dosierung) { this.dosierung = dosierung; }
    public String getEinnahmehinweis() { return einnahmehinweis; }
    public void setEinnahmehinweis(String einnahmehinweis) { this.einnahmehinweis = einnahmehinweis; }
    public LocalDate getAusstellungsdatum() { return ausstellungsdatum; }
    public void setAusstellungsdatum(LocalDate ausstellungsdatum) { this.ausstellungsdatum = ausstellungsdatum; }
    public LocalDate getGueltigBis() { return gueltigBis; }
    public void setGueltigBis(LocalDate gueltigBis) { this.gueltigBis = gueltigBis; }
    public RezeptStatus getStatus() { return status; }
    public void setStatus(RezeptStatus status) { this.status = status; }
}