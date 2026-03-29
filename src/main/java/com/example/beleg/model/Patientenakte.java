package com.example.beleg.model;

import jakarta.persistence.*;

/**
 * Entity-Klasse fuer die Patientenakte in der Arztpraxis.
 * Enthaelt die vollstaendige medizinische Vorgeschichte eines Patienten.
 * Jeder Patient hat genau eine Akte (1:1 Beziehung).
 */
@Entity
public class Patientenakte {

    /** Eindeutige ID der Patientenakte (automatisch generiert) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Patient dem diese Akte zugeordnet ist (eindeutig, Pflichtfeld) */
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    /** Krankengeschichte und Vorerkrankungen des Patienten */
    @Column(columnDefinition = "TEXT")
    private String anamnese;

    /** Bekannte Allergien des Patienten */
    @Column(columnDefinition = "TEXT")
    private String allergien;

    /**  Chronische Erkrankungen des Patienten */
    @Column(columnDefinition = "TEXT")
    private String chronischeKrankheiten;

    /** Aktuelle Medikation des Patienten */
    @Column(columnDefinition = "TEXT")
    private String medikamente;

    /**  Allgemeine Notizen zum Patienten */
    @Column(columnDefinition = "TEXT")
    private String notizen;

    /**Getters und Setters*/
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public String getAnamnese() { return anamnese; }
    public void setAnamnese(String anamnese) { this.anamnese = anamnese; }
    public String getAllergien() { return allergien; }
    public void setAllergien(String allergien) { this.allergien = allergien; }
    public String getChronischeKrankheiten() { return chronischeKrankheiten; }
    public void setChronischeKrankheiten(String chronischeKrankheiten) { this.chronischeKrankheiten = chronischeKrankheiten; }
    public String getMedikamente() { return medikamente; }
    public void setMedikamente(String medikamente) { this.medikamente = medikamente; }
    public String getNotizen() { return notizen; }
    public void setNotizen(String notizen) { this.notizen = notizen; }
}