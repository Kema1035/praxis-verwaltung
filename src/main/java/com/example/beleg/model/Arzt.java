package com.example.beleg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity-Klasse fuer einen Arzt in der Arztpraxis.
 * Speichert Name und medizinische Fachrichtung des Arztes.
 */
@Entity
public class Arzt {

    /** Eindeutige ID des Arztes (automatisch generiert) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Vollstaendiger Name des Arztes (2-100 Zeichen, Pflichtfeld) */
    @NotBlank @Size(min = 2, max = 100)
    private String name;

    /** Medizinische Fachrichtung des Arztes (2-100 Zeichen, Pflichtfeld) */
    @NotBlank @Size(min = 2, max = 100)
    private String fachrichtung;

    /**Getters und Setters+*/
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFachrichtung() { return fachrichtung; }
    public void setFachrichtung(String fachrichtung) { this.fachrichtung = fachrichtung; }
}