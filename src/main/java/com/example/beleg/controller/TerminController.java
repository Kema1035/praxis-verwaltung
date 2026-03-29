package com.example.beleg.controller;

import com.example.beleg.model.Arzt;
import com.example.beleg.model.Patient;
import com.example.beleg.model.Termin;
import com.example.beleg.repository.ArztRepository;
import com.example.beleg.repository.PatientRepository;
import com.example.beleg.repository.TerminRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller fuer die Verwaltung von Terminen.
 * Bietet CRUD-Operationen sowie Such- und Filterfunktionen nach Datum,
 * Zeitraum, Patientenname, Arztname und Status.
 * Prueft beim Speichern auf Doppelbuchungen des gleichen Arztes.
 */
@Controller
@RequestMapping("/termine")
public class TerminController {

    private final TerminRepository terminRepository;
    private final PatientRepository patientRepository;
    private final ArztRepository arztRepository;

    /**
     * Konstruktor mit Dependency Injection der benoedigten Repositories.
     *
     * @param terminRepository  Repository fuer Termin-Datenbankoperationen
     * @param patientRepository Repository fuer Patienten-Datenbankoperationen
     * @param arztRepository    Repository fuer Arzt-Datenbankoperationen
     */
    public TerminController(TerminRepository terminRepository,
                            PatientRepository patientRepository,
                            ArztRepository arztRepository) {
        this.terminRepository = terminRepository;
        this.patientRepository = patientRepository;
        this.arztRepository = arztRepository;
    }

    /**
     * Zeigt alle Termine mit optionalen Filtermoeglichkeiten an.
     * Filter koennen kombiniert werden: Datum, Zeitraum, Patientenname, Arztname oder Status.
     *
     * @param datum       Filter nach einem bestimmten Datum (optional)
     * @param von         Startdatum fuer Zeitraumfilter (optional)
     * @param bis         Enddatum fuer Zeitraumfilter (optional)
     * @param patientName Filter nach Patientenname (Teilstring, optional)
     * @param arztName    Filter nach Arztname (Teilstring, optional)
     * @param status      Filter nach Terminstatus (optional)
     * @param model       Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate von,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bis,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String arztName,
            @RequestParam(required = false) String status,
            Model model) {

        List<Termin> termine;

        if (von != null && bis != null) {
            termine = terminRepository.findByDatumBetween(von, bis);
        } else if (datum != null) {
            termine = terminRepository.findByDatum(datum);
        } else if (patientName != null && !patientName.isBlank()) {
            termine = terminRepository.findByPatientNameContainingIgnoreCase(patientName);
        } else if (arztName != null && !arztName.isBlank()) {
            termine = terminRepository.findByArztNameContainingIgnoreCase(arztName);
        } else if (status != null && !status.isBlank()) {
            termine = terminRepository.findByStatus(Termin.Status.valueOf(status));
        } else {
            termine = terminRepository.findAll();
        }

        model.addAttribute("termine", termine);
        model.addAttribute("datum", datum);
        model.addAttribute("von", von);
        model.addAttribute("bis", bis);
        model.addAttribute("patientName", patientName);
        model.addAttribute("arztName", arztName);
        model.addAttribute("status", status);
        model.addAttribute("statusWerte", Termin.Status.values());


        List<String> patientNamen = terminRepository.findAll().stream()
                .map(t -> t.getPatient().getName())
                .distinct()
                .sorted()
                .toList();
        List<String> arztNamen = terminRepository.findAll().stream()
                .map(t -> t.getArzt().getName())
                .distinct()
                .sorted()
                .toList();
        model.addAttribute("patientNamen", patientNamen);
        model.addAttribute("arztNamen", arztNamen);

        return "termine";
    }

    /**
     * Zeigt das Formular zum Erstellen eines neuen Termins an.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("termin", new Termin());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("aerzte", arztRepository.findAll());
        model.addAttribute("statusWerte", Termin.Status.values());
        model.addAttribute("alleTermine", terminRepository.findAll());
        return "termin_form";
    }

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Termins an.
     *
     * @param id    ID des zu bearbeitenden Termins
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     * @throws IllegalArgumentException wenn kein Termin mit der angegebenen ID gefunden wird
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Termin termin = terminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Termin ID"));
        model.addAttribute("termin", termin);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("aerzte", arztRepository.findAll());
        model.addAttribute("statusWerte", Termin.Status.values());
        model.addAttribute("alleTermine", terminRepository.findAll());
        return "termin_form";
    }

    /**
     * Speichert einen neuen oder bearbeiteten Termin in der Datenbank.
     * Prueft ob der Arzt zum angegebenen Zeitpunkt bereits einen Termin hat.
     * Bei Doppelbuchung wird eine Fehlermeldung angezeigt.
     *
     * @param formTermin Termin-Objekt mit den Formulardaten
     * @param model      Spring MVC Model fuer die View (bei Fehler)
     * @return Weiterleitung zur Terminliste oder zurueck zum Formular bei Doppelbuchung
     */
    @PostMapping
    public String save(@ModelAttribute Termin formTermin, Model model) {

        Termin termin;

        if (formTermin.getId() != null) {
            termin = terminRepository.findById(formTermin.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Termin ID"));
        } else {
            termin = new Termin();
        }

        Patient patient = patientRepository.findById(formTermin.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Patient ID"));

        Arzt arzt = arztRepository.findById(formTermin.getArzt().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Arzt ID"));

        boolean istFrei = terminRepository.findByArztIdAndDatumAndUhrzeit(
                        arzt.getId(), formTermin.getDatum(), formTermin.getUhrzeit())
                .map(t -> t.getId().equals(termin.getId()))
                .orElse(true);

        if (!istFrei) {
            model.addAttribute("error", "Dieser Arzt hat bereits einen Termin zu dieser Zeit!");
            model.addAttribute("termin", formTermin);
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("aerzte", arztRepository.findAll());
            model.addAttribute("statusWerte", Termin.Status.values());
            model.addAttribute("alleTermine", terminRepository.findAll());
            return "termin_form";
        }

        termin.setPatient(patient);
        termin.setArzt(arzt);
        termin.setDatum(formTermin.getDatum());
        termin.setUhrzeit(formTermin.getUhrzeit());
        termin.setStatus(formTermin.getStatus() != null ? formTermin.getStatus() : Termin.Status.GEPLANT);
        termin.setTerminArt(formTermin.getTerminArt() != null ? formTermin.getTerminArt() : Termin.TerminArt.ERSTUNTERSUCHUNG);

        terminRepository.save(termin);
        return "redirect:/termine";
    }

    /**
     * Loescht einen Termin anhand seiner ID.
     *
     * @param id ID des zu loeschenden Termins
     * @return Weiterleitung zur Terminliste
     */
    @GetMapping("/delete/{id}")
    @Transactional
    public String deletePatient(@PathVariable Long id) {
        terminRepository.deleteByPatientId(id);
        patientRepository.deleteById(id);
        return "redirect:/patients";
    }
}