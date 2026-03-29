package com.example.beleg.controller;

import com.example.beleg.model.Arzt;
import com.example.beleg.repository.ArztRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller fuer die Verwaltung von Aerzten.
 * Bietet CRUD-Operationen sowie eine Duplikatpruefung beim Speichern.
 */
@Controller
@RequestMapping("/aerzte")
public class ArztController {

    private final ArztRepository arztRepository;

    /**
     * Konstruktor mit Dependency Injection des Arzt-Repositories.
     *
     * @param arztRepository Repository fuer Arzt-Datenbankoperationen
     */
    public ArztController(ArztRepository arztRepository) {
        this.arztRepository = arztRepository;
    }

    /**
     * Zeigt eine Liste aller Aerzte an.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("aerzte", arztRepository.findAll());
        return "aerzte";
    }

    /**
     * Zeigt das Formular zum Erstellen eines neuen Arztes an.
     *
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("arzt", new Arzt());
        return "arzt_form";
    }

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Arztes an.
     *
     * @param id    ID des zu bearbeitenden Arztes
     * @param model Spring MVC Model fuer die View
     * @return Name des Thymeleaf-Templates fuer das Formular
     * @throws IllegalArgumentException wenn kein Arzt mit der angegebenen ID gefunden wird
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Arzt arzt = arztRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Arzt ID"));
        model.addAttribute("arzt", arzt);
        return "arzt_form";
    }

    /**
     * Speichert einen neuen oder bearbeiteten Arzt in der Datenbank.
     * Prueft beim Erstellen ob bereits ein Arzt mit gleichem Namen
     * und gleicher Fachrichtung existiert.
     *
     * @param arzt   Arzt-Objekt mit den validierten Formulardaten
     * @param result BindingResult fuer Validierungsfehler
     * @return Weiterleitung zur Arztliste oder zurueck zum Formular bei Fehler
     */
    @PostMapping
    public String save(@Valid @ModelAttribute Arzt arzt, BindingResult result) {
        if (arzt.getId() == null &&
                arztRepository.existsByNameAndFachrichtung(arzt.getName(), arzt.getFachrichtung())) {
            result.rejectValue("name", "duplicate",
                    "Ein Arzt mit diesem Namen und dieser Fachrichtung existiert bereits");
        }

        if (result.hasErrors()) {
            return "arzt_form";
        }

        arztRepository.save(arzt);
        return "redirect:/aerzte";
    }

    /**
     * Loescht einen Arzt anhand seiner ID.
     *
     * @param id ID des zu loeschenden Arztes
     * @return Weiterleitung zur Arztliste
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        arztRepository.deleteById(id);
        return "redirect:/aerzte";
    }
}