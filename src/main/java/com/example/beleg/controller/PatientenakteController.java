package com.example.beleg.controller;

import com.example.beleg.model.*;
import com.example.beleg.repository.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller fuer die Verwaltung von Patientenakten und Diagnosen.
 * Bietet Funktionen zum Anzeigen und Bearbeiten der medizinischen Akte
 * eines Patienten sowie zum Verwalten von Diagnosen.
 */
@Controller
@RequestMapping("/patients")
public class PatientenakteController {

    private final PatientRepository patientRepository;
    private final PatientenakteRepository patientenakteRepository;
    private final DiagnoseRepository diagnoseRepository;
    private final TerminRepository terminRepository;
    private final ArztRepository arztRepository;

    /**
     * Konstruktor mit Dependency Injection der benoedigten Repositories.
     *
     * @param patientRepository        Repository fuer Patienten-Datenbankoperationen
     * @param patientenakteRepository  Repository fuer Patientenakte-Datenbankoperationen
     * @param diagnoseRepository       Repository fuer Diagnose-Datenbankoperationen
     * @param terminRepository         Repository fuer Termin-Datenbankoperationen
     * @param arztRepository           Repository fuer Arzt-Datenbankoperationen
     */
    public PatientenakteController(PatientRepository patientRepository,
                                   PatientenakteRepository patientenakteRepository,
                                   DiagnoseRepository diagnoseRepository,
                                   TerminRepository terminRepository,
                                   ArztRepository arztRepository) {
        this.patientRepository = patientRepository;
        this.patientenakteRepository = patientenakteRepository;
        this.diagnoseRepository = diagnoseRepository;
        this.terminRepository = terminRepository;
        this.arztRepository = arztRepository;
    }


    /**
     * Zeigt die Patientenakte eines Patienten an.
     * Falls noch keine Akte existiert, wird ein leeres Objekt erstellt.
     *
     * @param id    ID des Patienten
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     * @throws IllegalArgumentException wenn kein Patient mit der angegebenen ID gefunden wird
     */
    @GetMapping("/{id}/akte")
    public String showAkte(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        Patientenakte akte = patientenakteRepository.findByPatientId(id)
                .orElse(new Patientenakte());
        akte.setPatient(patient);

        model.addAttribute("patient", patient);
        model.addAttribute("akte", akte);
        model.addAttribute("diagnosen", diagnoseRepository.findByPatientIdOrderByDatumDesc(id));
        model.addAttribute("termine", terminRepository.findByPatientNameContainingIgnoreCase(patient.getName()));
        return "patientenakte";
    }

    /**
     * Speichert die Patientenakte eines Patienten.
     * Falls noch keine Akte existiert, wird eine neue erstellt.
     *
     * @param id       ID des Patienten
     * @param formAkte Patientenakte-Objekt mit den Formulardaten
     * @return Weiterleitung zur Patientenakte
     * @throws IllegalArgumentException wenn kein Patient mit der angegebenen ID gefunden wird
     */
    @PostMapping("/{id}/akte")
    public String saveAkte(@PathVariable Long id, @ModelAttribute Patientenakte formAkte) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        Patientenakte akte = patientenakteRepository.findByPatientId(id)
                .orElse(new Patientenakte());

        akte.setPatient(patient);
        akte.setAnamnese(formAkte.getAnamnese());
        akte.setAllergien(formAkte.getAllergien());
        akte.setChronischeKrankheiten(formAkte.getChronischeKrankheiten());
        akte.setMedikamente(formAkte.getMedikamente());
        akte.setNotizen(formAkte.getNotizen());

        patientenakteRepository.save(akte);
        return "redirect:/patients/" + id + "/akte";
    }


    /**
     * Zeigt das Formular zum Erstellen einer neuen Diagnose fuer einen Patienten an.
     *
     * @param id    ID des Patienten
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Diagnoseformular
     * @throws IllegalArgumentException wenn kein Patient mit der angegebenen ID gefunden wird
     */
    @GetMapping("/{id}/diagnose/new")
    public String newDiagnoseForm(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        Diagnose diagnose = new Diagnose();
        diagnose.setPatient(patient);
        diagnose.setDatum(LocalDate.now());

        model.addAttribute("patient", patient);
        model.addAttribute("diagnose", diagnose);
        model.addAttribute("aerzte", arztRepository.findAll());
        model.addAttribute("termine", terminRepository.findByPatientNameContainingIgnoreCase(patient.getName()));
        return "diagnose_form";
    }

    /**
     * Zeigt das Formular zum Bearbeiten einer bestehenden Diagnose an.
     *
     * @param patientId  ID des Patienten
     * @param diagnoseId ID der zu bearbeitenden Diagnose
     * @param model      Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Diagnoseformular
     * @throws IllegalArgumentException wenn Patient oder Diagnose nicht gefunden werden
     */
    @GetMapping("/{patientId}/diagnose/edit/{diagnoseId}")
    public String editDiagnoseForm(@PathVariable Long patientId,
                                   @PathVariable Long diagnoseId, Model model) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
        Diagnose diagnose = diagnoseRepository.findById(diagnoseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid diagnose ID"));

        model.addAttribute("patient", patient);
        model.addAttribute("diagnose", diagnose);
        model.addAttribute("aerzte", arztRepository.findAll());
        model.addAttribute("termine", terminRepository.findByPatientNameContainingIgnoreCase(patient.getName()));
        return "diagnose_form";
    }

    /**
     * Speichert eine neue oder bearbeitete Diagnose fuer einen Patienten.
     *
     * @param id              ID des Patienten
     * @param diagnoseId      ID der Diagnose (null bei neuer Diagnose)
     * @param datum           Datum der Diagnosestellung
     * @param diagnoseText    Bezeichnung der Diagnose
     * @param beschreibung    Detaillierte Beschreibung (optional)
     * @param behandlungsplan Geplanter Behandlungsplan (optional)
     * @param arztId          ID des behandelnden Arztes (optional)
     * @param terminId        ID des zugehoerigen Termins (optional)
     * @return Weiterleitung zur Patientenakte
     * @throws IllegalArgumentException wenn kein Patient mit der angegebenen ID gefunden wird
     */
    @PostMapping("/{id}/diagnose")
    public String saveDiagnose(
            @PathVariable Long id,
            @RequestParam(required = false) Long diagnoseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum,
            @RequestParam String diagnoseText,
            @RequestParam(required = false) String beschreibung,
            @RequestParam(required = false) String behandlungsplan,
            @RequestParam(required = false) Long arztId,
            @RequestParam(required = false) Long terminId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        Diagnose diagnose = (diagnoseId != null)
                ? diagnoseRepository.findById(diagnoseId).orElse(new Diagnose())
                : new Diagnose();

        diagnose.setPatient(patient);
        diagnose.setDatum(datum);
        diagnose.setDiagnose(diagnoseText);
        diagnose.setBeschreibung(beschreibung);
        diagnose.setBehandlungsplan(behandlungsplan);

        if (arztId != null) {
            arztRepository.findById(arztId).ifPresent(diagnose::setArzt);
        }
        if (terminId != null) {
            terminRepository.findById(terminId).ifPresent(diagnose::setTermin);
        }

        diagnoseRepository.save(diagnose);
        return "redirect:/patients/" + id + "/akte";
    }

    /**
     * Loescht eine Diagnose anhand ihrer ID.
     *
     * @param patientId  ID des Patienten (fuer Weiterleitung)
     * @param diagnoseId ID der zu loeschenden Diagnose
     * @return Weiterleitung zur Patientenakte
     */
    @GetMapping("/{patientId}/diagnose/delete/{diagnoseId}")
    public String deleteDiagnose(@PathVariable Long patientId, @PathVariable Long diagnoseId) {
        diagnoseRepository.deleteById(diagnoseId);
        return "redirect:/patients/" + patientId + "/akte";
    }
}