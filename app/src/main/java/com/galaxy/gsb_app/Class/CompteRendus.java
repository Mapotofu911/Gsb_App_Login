package com.galaxy.gsb_app.Class;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mapotofu on 07/03/2017.
 */

public class CompteRendus {

    private int id;
    private String practicien_nom;
    private int pract_id;
    private List<Medicaments> medicamentsPresente_rapport;
    private String date_rapport;
    private List<Medicaments> medicamentsOfferts_rapport;
    private String motif;
    private Boolean remplacant;
    private Boolean documentation;
    private Boolean SaisieDefinitive;
    private String bilan;
    private int visiteur_rapport_id;

    public CompteRendus(int id, String practicien_nom, int pract_id, List<Medicaments> medicamentsPresente_rapport, String date_rapport, List<Medicaments> medicamentsOfferts_rapport, String motif, Boolean remplacant, Boolean documentation, Boolean saisieDefinitive, String bilan, int visiteur_rapport_id) {

        this.id = id;
        this.practicien_nom = practicien_nom;
        this.pract_id = pract_id;
        this.medicamentsPresente_rapport = medicamentsPresente_rapport;
        this.date_rapport = date_rapport;
        this.medicamentsOfferts_rapport = medicamentsOfferts_rapport;
        this.motif = motif;
        this.remplacant = remplacant;
        this.documentation = documentation;
        SaisieDefinitive = saisieDefinitive;
        this.bilan = bilan;
        this.visiteur_rapport_id = visiteur_rapport_id;
        Date date = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        this.date_rapport = df.format(date);
    }

    public int getPract_id() {
        return pract_id;
    }

    public void setPract_id(int pract_id) {
        this.pract_id = pract_id;
    }

    public String getPracticien_nom() {
        return practicien_nom;
    }

    public void setPracticien_nom(String practicien_nom) {
        this.practicien_nom = practicien_nom;
    }

    public int getVisiteur_rapport_id() {
        return visiteur_rapport_id;
    }

    public void setVisiteur_rapport_id(int visiteur_rapport_id) {
        this.visiteur_rapport_id = visiteur_rapport_id;
    }

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public CompteRendus() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Medicaments> getMedicamentsPresente_rapport() {
        return medicamentsPresente_rapport;
    }

    public void setMedicamentsPresente_rapport(List<Medicaments> medicamentsPresente_rapport) {
        this.medicamentsPresente_rapport = medicamentsPresente_rapport;
    }

    public String getDate_rapport() {
        return String.valueOf(date_rapport);
    }

    public void setDate_rapport(String date_rapport) {
        this.date_rapport = date_rapport;
    }

    public List<Medicaments> getMedicamentsOfferts_rapport() {
        return medicamentsOfferts_rapport;
    }

    public void setMedicamentsOfferts_rapport(List<Medicaments> medicamentsOfferts_rapport) {
        this.medicamentsOfferts_rapport = medicamentsOfferts_rapport;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Boolean getRemplacant() {
        return remplacant;
    }

    public void setRemplacant(Boolean remplacant) {
        this.remplacant = remplacant;
    }

    public Boolean getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Boolean documentation) {
        this.documentation = documentation;
    }

    public Boolean getSaisieDefinitive() {
        return SaisieDefinitive;
    }

    public void setSaisieDefinitive(Boolean saisieDefinitive) {
        SaisieDefinitive = saisieDefinitive;
    }
}
