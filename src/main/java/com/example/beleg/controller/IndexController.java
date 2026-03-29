package com.example.beleg.controller;

import com.example.beleg.repository.ArztRepository;
import com.example.beleg.repository.PatientRepository;
import com.example.beleg.repository.TerminRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller fuer die Startseite (Dashboard) der Arztpraxis-Anwendung.
 * Zeigt eine Uebersicht mit Statistiken zu Patienten, Aerzten und Terminen.
 */
@Controller
public class IndexController {

    private final PatientRepository patientRepository;
    private final ArztRepository arztRepository;
    private final TerminRepository terminRepository;

    /**
     * Konstruktor mit Dependency Injection der benoedigten Repositories.
     *
     * @param patientRepository Repository fuer Patienten-Datenbankoperationen
     * @param arztRepository    Repository fuer Arzt-Datenbankoperationen
     * @param terminRepository  Repository fuer Termin-Datenbankoperationen
     */
    public IndexController(PatientRepository patientRepository,
                           ArztRepository arztRepository,
                           TerminRepository terminRepository) {
        this.patientRepository = patientRepository;
        this.arztRepository = arztRepository;
        this.terminRepository = terminRepository;
    }

    /**
     * Zeigt das Dashboard mit allgemeinen Statistiken an.
     * Enthaelt Gesamtanzahl von Patienten, Aerzten und Terminen,
     * heutige Termine sowie eine Statistik der Termine pro Arzt.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Willkommen zum Arztpraxis System");

        model.addAttribute("patientenCount", patientRepository.count());
        model.addAttribute("aerzteCount", arztRepository.count());
        model.addAttribute("termineCount", terminRepository.count());
        model.addAttribute("termineHeuteCount", terminRepository.findByDatum(LocalDate.now()).size());

        List<Object[]> rawStats = terminRepository.countTermineByArzt();
        List<Map<String, Object>> arztStats = new ArrayList<>();
        for (Object[] row : rawStats) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("name", row[0]);
            entry.put("fachrichtung", row[1]);
            entry.put("count", row[2]);
            arztStats.add(entry);
        }
        model.addAttribute("arztStats", arztStats);

        return "index";
    }
}