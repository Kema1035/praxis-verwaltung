package com.example.beleg.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity-Klasse fuer eine Diagnose in der Arztpraxis.
 * Eine Diagnose wird einem Patienten zugeordnet und enthaelt
 * den Befund, eine detaillierte Beschreibung sowie den Behandlungsplan.
 */
@Entity
public class Diagnose {

    /** Eindeutige ID der Diagnose (automatisch generiert) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Patient dem diese Diagnose zugeordnet ist */
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** Termin bei dem die Diagnose gestellt wurde (optional) */
    @ManyToOne
    @JoinColumn(name = "termin_id")
    private Termin termin;

    /** Arzt der die Diagnose gestellt hat (optional) */
    @ManyToOne
    @JoinColumn(name = "arzt_id")
    private Arzt arzt;

    /** Datum der Diagnosestellung */
    private LocalDate datum;

    /** Bezeichnung der Diagnose */
    private String diagnose;

    /**  Detaillierte Beschreibung der Diagnose */
    @Column(columnDefinition = "TEXT")
    private String beschreibung;

    /** Geplanter Behandlungsplan */
    @Column(columnDefinition = "TEXT")
    private String behandlungsplan;

    /** Getters und Setters*/
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Termin getTermin() { return termin; }
    public void setTermin(Termin termin) { this.termin = termin; }
    public Arzt getArzt() { return arzt; }
    public void setArzt(Arzt arzt) { this.arzt = arzt; }
    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }
    public String getDiagnose() { return diagnose; }
    public void setDiagnose(String diagnose) { this.diagnose = diagnose; }
    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }
    public String getBehandlungsplan() { return behandlungsplan; }
    public void setBehandlungsplan(String behandlungsplan) { this.behandlungsplan = behandlungsplan; }
}