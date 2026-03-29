package com.example.beleg.controller;

import com.example.beleg.model.Patient;
import com.example.beleg.repository.PatientRepository;
import com.example.beleg.repository.TerminRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller fuer die Verwaltung von Patienten.
 * Bietet CRUD-Operationen, Detailansicht, Suche sowie
 * eine Duplikatpruefung beim Erstellen neuer Patienten.
 */
@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final TerminRepository terminRepository;

    /**
     * Konstruktor mit Dependency Injection der benoedigten Repositories.
     *
     * @param patientRepository Repository fuer Patienten-Datenbankoperationen
     * @param terminRepository  Repository fuer Termin-Datenbankoperationen
     */
    public PatientController(PatientRepository patientRepository,
                             TerminRepository terminRepository) {
        this.patientRepository = patientRepository;
        this.terminRepository = terminRepository;
    }

    /**
     * Zeigt eine Liste aller Patienten an.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     */
    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "patients";
    }

    /**
     * Zeigt die Detailseite eines Patienten mit Terminhistorie an.
     *
     * @param id    ID des Patienten
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer die Detailansicht
     * @throws IllegalArgumentException wenn kein Patient mit der angegebenen ID gefunden wird
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
        model.addAttribute("patient", patient);
        model.addAttribute("termine", terminRepository.findByPatientNameContainingIgnoreCase(patient.getName()));
        return "patient_detail";
    }

    /**
     * Zeigt das Formular zum Erstellen eines neuen Patienten an.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient_form";
    }

    /**
     * Speichert einen neuen oder bearbeiteten Patienten in der Datenbank.
     * Prueft beim Erstellen ob bereits ein Patient mit gleichem Namen
     * und gleichem Geburtsdatum existiert.
     *
     * @param patient Patienten-Objekt mit den validierten Formulardaten
     * @param result  BindingResult fuer Validierungsfehler
     * @return Weiterleitung zur Patientenliste oder zurueck zum Formular bei Fehler
     */
    @PostMapping
    public String savePatient(@Valid @ModelAttribute Patient patient,
                              BindingResult result) {
        if (patient.getId() == null &&
                patientRepository.existsByNameAndGeburtsdatum(
                        patient.getName(), patient.getGeburtsdatum())) {
            result.rejectValue("name", "duplicate",
                    "Patient mit diesem Namen und Geburtsdatum existiert bereits");
        }

        if (result.hasErrors()) {
            return "patient_form";
        }

        patientRepository.save(patient);
        return "redirect:/patients";
    }

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Patienten an.
     *
     * @param id    ID des zu bearbeitenden Patienten
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     * @throws IllegalArgumentException wenn kein Patient mit der angegebenen ID gefunden wird
     */
    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
        model.addAttribute("patient", patient);
        return "patient_form";
    }

    /**
     * Loescht einen Patienten anhand seiner ID.
     * Zuvor werden alle zugehoerigen Termine geloescht.
     *
     * @param id ID des zu loeschenden Patienten
     * @return Weiterleitung zur Patientenliste
     */
    @GetMapping("/delete/{id}")
    @org.springframework.transaction.annotation.Transactional
    public String deletePatient(@PathVariable Long id) {
        terminRepository.deleteByPatientId(id);  // erst Termine loeschen
        patientRepository.deleteById(id);         // dann Patient loeschen
        return "redirect:/patients";
    }

    /**
     * Sucht Patienten anhand eines Suchbegriffs im Namen.
     *
     * @param keyword Suchbegriff fuer die Namenssuche
     * @param model   Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates mit den Suchergebnissen
     */
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("patients",
                patientRepository.findByNameContainingIgnoreCase(keyword));
        return "patients";
    }
}