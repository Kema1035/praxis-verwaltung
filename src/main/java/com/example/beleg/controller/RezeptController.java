package com.example.beleg.controller;

import com.example.beleg.model.*;
import com.example.beleg.repository.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller fuer die Verwaltung von Rezepten.
 * Ermoeglicht das Ausstellen, Bearbeiten und Loeschen von Rezepten.
 * Ein Rezept kann optional mit einem Termin verknuepft werden.
 */
@Controller
@RequestMapping("/rezepte")
public class RezeptController {

    private final RezeptRepository rezeptRepository;
    private final PatientRepository patientRepository;
    private final ArztRepository arztRepository;
    private final TerminRepository terminRepository;

    /**
     * Konstruktor mit Dependency Injection der benoedigten Repositories.
     *
     * @param rezeptRepository  Repository fuer Rezept-Datenbankoperationen
     * @param patientRepository Repository fuer Patienten-Datenbankoperationen
     * @param arztRepository    Repository fuer Arzt-Datenbankoperationen
     * @param terminRepository  Repository fuer Termin-Datenbankoperationen
     */
    public RezeptController(RezeptRepository rezeptRepository,
                            PatientRepository patientRepository,
                            ArztRepository arztRepository,
                            TerminRepository terminRepository) {
        this.rezeptRepository = rezeptRepository;
        this.patientRepository = patientRepository;
        this.arztRepository = arztRepository;
        this.terminRepository = terminRepository;
    }

    /**
     * Zeigt eine Liste aller ausgestellten Rezepte an.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     */

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rezepte", rezeptRepository.findAll());
        return "rezepte";
    }

    /**
     * Zeigt das Formular zum Erstellen eines neuen Rezepts an.
     * Optional kann eine Patienten-ID uebergeben werden um das Patientenfeld vorzubefuellen.
     *
     * @param patientId ID des Patienten (optional)
     * @param model     Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     */

    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long patientId, Model model) {
        Rezept rezept = new Rezept();
        rezept.setAusstellungsdatum(LocalDate.now());
        rezept.setGueltigBis(LocalDate.now().plusDays(30));

        if (patientId != null) {
            patientRepository.findById(patientId).ifPresent(rezept::setPatient);
        }

        model.addAttribute("rezept", rezept);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("aerzte", arztRepository.findAll());
        model.addAttribute("termine", terminRepository.findAll());
        return "rezept_form";
    }

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Rezepts an.
     *
     * @param id    ID des zu bearbeitenden Rezepts
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     * @throws IllegalArgumentException wenn kein Rezept mit der angegebenen ID gefunden wird
     */

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Rezept rezept = rezeptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rezept ID"));
        model.addAttribute("rezept", rezept);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("aerzte", arztRepository.findAll());
        model.addAttribute("termine", terminRepository.findAll());
        return "rezept_form";
    }

    /**
     * Speichert ein neues oder bearbeitetes Rezept in der Datenbank.
     * Das Gueltigkeitsdatum wird standardmaessig auf 30 Tage nach Ausstellung gesetzt.
     *
     * @param rezeptId          ID des Rezepts (null bei neuem Rezept)
     * @param patientId         ID des Patienten
     * @param arztId            ID des ausstellenden Arztes
     * @param terminId          ID des zugehoerigen Termins (optional)
     * @param medikament        Name des Medikaments
     * @param dosierung         Dosierungsangabe (optional)
     * @param einnahmehinweis   Hinweise zur Einnahme (optional)
     * @param ausstellungsdatum Datum der Ausstellung
     * @param gueltigBis        Datum bis das Rezept gueltig ist
     * @param status            Status des Rezepts
     * @return Weiterleitung zur Rezeptliste
     */

    @PostMapping
    public String save(
            @RequestParam(required = false) Long rezeptId,
            @RequestParam Long patientId,
            @RequestParam Long arztId,
            @RequestParam(required = false) Long terminId,
            @RequestParam String medikament,
            @RequestParam(required = false) String dosierung,
            @RequestParam(required = false) String einnahmehinweis,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ausstellungsdatum,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate gueltigBis,
            @RequestParam String status) {

        Rezept rezept = (rezeptId != null)
                ? rezeptRepository.findById(rezeptId).orElse(new Rezept())
                : new Rezept();

        patientRepository.findById(patientId).ifPresent(rezept::setPatient);
        arztRepository.findById(arztId).ifPresent(rezept::setArzt);
        if (terminId != null) terminRepository.findById(terminId).ifPresent(rezept::setTermin);

        rezept.setMedikament(medikament);
        rezept.setDosierung(dosierung);
        rezept.setEinnahmehinweis(einnahmehinweis);
        rezept.setAusstellungsdatum(ausstellungsdatum);
        rezept.setGueltigBis(gueltigBis);
        rezept.setStatus(Rezept.RezeptStatus.valueOf(status));

        rezeptRepository.save(rezept);
        return "redirect:/rezepte";
    }

    /**
     * Loescht ein Rezept anhand seiner ID.
     *
     * @param id ID des zu loeschenden Rezepts
     * @return Weiterleitung zur Rezeptliste
     */

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        rezeptRepository.deleteById(id);
        return "redirect:/rezepte";
    }
}