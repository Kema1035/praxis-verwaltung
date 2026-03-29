package com.example.beleg.controller;

import com.example.beleg.model.Termin;
import com.example.beleg.repository.TerminRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Controller fuer die Kalenderansicht der Arztpraxis.
 * Zeigt Termine in einer woechentlichen Rasteransicht (Montag bis Freitag) an.
 * Zeitslots werden von 08:00 bis 17:30 Uhr in 30-Minuten-Schritten generiert.
 */
@Controller
@RequestMapping("/kalender")
public class KalenderController {

    private final TerminRepository terminRepository;

    /**
     * Konstruktor mit Dependency Injection des Termin-Repositories.
     *
     * @param terminRepository Repository fuer Termin-Datenbankoperationen
     */
    public KalenderController(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }

    /**
     * Zeigt die Kalenderansicht fuer eine bestimmte Woche an.
     * Ohne Datumsangabe wird die aktuelle Woche angezeigt.
     * Termine werden den naechsten passenden Zeitslots zugeordnet.
     *
     * @param datum Ein beliebiges Datum der anzuzeigenden Woche (optional)
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     */
    @GetMapping
    public String kalender(@RequestParam(required = false) LocalDate datum, Model model) {


        if (datum == null) datum = LocalDate.now();


        LocalDate montag = datum.with(WeekFields.ISO.dayOfWeek(), 1);
        LocalDate freitag = montag.plusDays(4);


        List<Termin> termine = terminRepository.findByDatumBetween(montag, freitag);


        List<LocalTime> zeitslots = new ArrayList<>();
        LocalTime time = LocalTime.of(8, 0);
        while (!time.isAfter(LocalTime.of(17, 30))) {
            zeitslots.add(time);
            time = time.plusMinutes(30);
        }


        List<LocalDate> wochentage = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            wochentage.add(montag.plusDays(i));
        }


        Map<String, List<Termin>> kalenderMap = new HashMap<>();
        for (Termin t : termine) {
            LocalTime slot = zeitslots.stream()
                    .filter(s -> !s.isAfter(t.getUhrzeit()))
                    .reduce((first, second) -> second)
                    .orElse(zeitslots.get(0));
            String key = t.getDatum() + "_" + slot;
            kalenderMap.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        model.addAttribute("montag", montag);
        model.addAttribute("freitag", freitag);
        model.addAttribute("wochentage", wochentage);
        model.addAttribute("zeitslots", zeitslots);
        model.addAttribute("kalenderMap", kalenderMap);
        model.addAttribute("vorwoche", montag.minusWeeks(1));
        model.addAttribute("nachwoche", montag.plusWeeks(1));
        model.addAttribute("heute", LocalDate.now());

        return "kalender";
    }
}