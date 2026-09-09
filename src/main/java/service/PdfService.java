package com.example.demo.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.example.demo.entity.Note;
import com.example.demo.entity.Utilisateur;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    public byte[] genererReleveNotes(Utilisateur eleve, List<Note> notes, double moyenneGenerale) {
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        ByteArrayOutputStream sortie = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, sortie);
            document.open();

            Font policeTitre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(30, 58, 95));
            Font policeSousTitre = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.GRAY);
            Font policeEntete = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Font policeCorps = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font policeMoyenne = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new Color(30, 58, 95));

            Paragraph titre = new Paragraph("EduTrack - Grade Report", policeTitre);
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            Paragraph eleveInfo = new Paragraph(eleve.getPrenom() + " " + eleve.getNom() + " - " + eleve.getEmail(), policeSousTitre);
            eleveInfo.setAlignment(Element.ALIGN_CENTER);
            eleveInfo.setSpacingAfter(20);
            document.add(eleveInfo);

            PdfPTable tableau = new PdfPTable(4);
            tableau.setWidthPercentage(100);
            tableau.setWidths(new float[]{3, 2, 1.5f, 2});

            String[] entetes = {"Subject", "Exam Type", "Grade", "Date"};
            for (String entete : entetes) {
                PdfPCell cellule = new PdfPCell(new Phrase(entete, policeEntete));
                cellule.setBackgroundColor(new Color(30, 58, 95));
                cellule.setPadding(8);
                tableau.addCell(cellule);
            }

            for (Note note : notes) {
                tableau.addCell(new PdfPCell(new Phrase(note.getMatiere().getNom(), policeCorps)) {{ setPadding(6); }});
                tableau.addCell(new PdfPCell(new Phrase(note.getTypeExamen(), policeCorps)) {{ setPadding(6); }});
                tableau.addCell(new PdfPCell(new Phrase(note.getValeur() + " / 20", policeCorps)) {{ setPadding(6); }});
                tableau.addCell(new PdfPCell(new Phrase(note.getDate().toString(), policeCorps)) {{ setPadding(6); }});
            }

            document.add(tableau);

            Paragraph moyenne = new Paragraph("\nOverall average: " + String.format("%.2f", moyenneGenerale) + " / 20", policeMoyenne);
            moyenne.setSpacingBefore(20);
            document.add(moyenne);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating the PDF", e);
        }

        return sortie.toByteArray();
    }
}