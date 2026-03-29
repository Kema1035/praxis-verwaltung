package com.example.beleg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entity-Klasse fuer einen Patienten in der Arztpraxis.
 * Speichert persoenliche Daten sowie die Art der Krankenversicherung.
 */
@Entity
public class Patient {

    /** Eindeutige ID des Patienten (automatisch generiert) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Vollstaendiger Name des Patienten (Pflichtfeld) */
    @NotBlank
    private String name;

    /** Geburtsdatum des Patienten */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate geburtsdatum;

    /** Telefonnummer des Patienten */
    private String telefon;

    /** Art der Krankenversicherung, Standardwert: GESETZLICH */
    @Enumerated(EnumType.STRING)
    private Versicherung versicherung = Versicherung.GESETZLICH;

    /**
     * Enum fuer die verschiedenen Versicherungsarten.
     */
    public enum Versicherung {
        /** Gesetzliche Krankenversicherung (GKV) */
        GESETZLICH,
        /** Private Krankenversicherung (PKV) */
        PRIVAT,
        /** Beihilfe (z.B. fuer Beamte) */
        BEIHILFE,
        /** Patient zahlt selbst ohne Versicherung */
        SELBSTZAHLER
    }

    /** Getters und Setters*/
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getGeburtsdatum() { return geburtsdatum; }
    public void setGeburtsdatum(LocalDate geburtsdatum) { this.geburtsdatum = geburtsdatum; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public Versicherung getVersicherung() { return versicherung; }
    public void setVersicherung(Versicherung versicherung) { this.versicherung = versicherung; }
}