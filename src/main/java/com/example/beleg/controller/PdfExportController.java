package com.example.beleg.controller;

import com.example.beleg.model.Termin;
import com.example.beleg.repository.TerminRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller fuer den PDF-Export von Terminen.
 * Generiert eine PDF-Datei mit einer gefilterten Terminliste
 * und sendet diese direkt als Download an den Browser.
 * Verwendet die iText-Bibliothek zur PDF-Erstellung.
 */
@Controller
@RequestMapping("/export")
public class PdfExportController {

    private final TerminRepository terminRepository;

    /**
     * Konstruktor mit Dependency Injection des Termin-Repositories.
     *
     * @param terminRepository Repository fuer Termin-Datenbankoperationen
     */
    public PdfExportController(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }

    /**
     * Exportiert Termine als PDF-Datei.
     * Unterstuetzt optionale Filter nach Zeitraum und Status.
     * Die Tabelle wird im Querformat (A4 landscape) mit alternierenden Zeilenfarben erstellt.
     *
     * @param von      Startdatum fuer Zeitraumfilter (optional)
     * @param bis      Enddatum fuer Zeitraumfilter (optional)
     * @param status   Filter nach Terminstatus (optional)
     * @param response HttpServletResponse fuer den PDF-Download
     * @throws IOException       bei Schreibfehlern in den Response-Stream
     * @throws DocumentException bei Fehlern bei der PDF-Erstellung
     */
    @GetMapping("/termine/pdf")
    public void exportPdf(@RequestParam(required = false) LocalDate von,
                          @RequestParam(required = false) LocalDate bis,
                          @RequestParam(required = false) String status,
                          HttpServletResponse response) throws IOException, DocumentException {


        List<Termin> termine;
        if (von != null && bis != null) {
            termine = terminRepository.findByDatumBetween(von, bis);
        } else if (status != null && !status.isBlank()) {
            termine = terminRepository.findByStatus(Termin.Status.valueOf(status));
        } else {
            termine = terminRepository.findAll();
        }


        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=termine.pdf");


        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();


        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 9);

        Paragraph title = new Paragraph("Terminliste - Arztpraxis System", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);


        Font dateFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
        Paragraph dateInfo = new Paragraph("Erstellt am: " + LocalDate.now(), dateFont);
        dateInfo.setAlignment(Element.ALIGN_CENTER);
        dateInfo.setSpacingAfter(15);
        document.add(dateInfo);


        if (von != null && bis != null) {
            Paragraph filterInfo = new Paragraph("Zeitraum: " + von + " bis " + bis, dateFont);
            filterInfo.setAlignment(Element.ALIGN_CENTER);
            filterInfo.setSpacingAfter(10);
            document.add(filterInfo);
        }


        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 3f, 2f, 2f, 2f});


        BaseColor headerColor = new BaseColor(33, 37, 41);
        String[] headers = {"Patient", "Arzt", "Datum", "Uhrzeit", "Status"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }


        boolean alternate = false;
        BaseColor lightGray = new BaseColor(240, 240, 240);
        for (Termin t : termine) {
            BaseColor rowColor = alternate ? lightGray : BaseColor.WHITE;

            PdfPCell c1 = new PdfPCell(new Phrase(t.getPatient().getName(), cellFont));
            PdfPCell c2 = new PdfPCell(new Phrase(t.getArzt().getName(), cellFont));
            PdfPCell c3 = new PdfPCell(new Phrase(t.getDatum().toString(), cellFont));
            PdfPCell c4 = new PdfPCell(new Phrase(t.getUhrzeit().toString(), cellFont));

            String statusText = switch (t.getStatus()) {
                case GEPLANT -> "Geplant";
                case BESTAETIGT -> "Bestaetigt";
                case ABGESAGT -> "Abgesagt";
            };
            PdfPCell c5 = new PdfPCell(new Phrase(statusText, cellFont));

            for (PdfPCell cell : new PdfPCell[]{c1, c2, c3, c4, c5}) {
                cell.setBackgroundColor(rowColor);
                cell.setPadding(6);
            }

            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);

            alternate = !alternate;
        }

        document.add(table);


        Paragraph total = new Paragraph("Gesamt: " + termine.size() + " Termine", dateFont);
        total.setSpacingBefore(10);
        document.add(total);

        document.close();
    }
}