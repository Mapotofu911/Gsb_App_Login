package com.galaxy.gsb_app.Class;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mapotofu on 22/03/2017.
 */

public final class CompteRenduSingleton {

    private int id;
    private String practicien_nom;
    private List<Medicaments> medicamentsPresente_rapport;
    private String date_rapport;
    private List<Medicaments> medicamentsOfferts_rapport;
    private String motif;
    private Boolean remplacant;
    private Boolean documentation;
    private Boolean SaisieDefinitive;
    private String bilan;
    private int visiteur_rapport_id;

    private static volatile CompteRenduSingleton instance  = new CompteRenduSingleton();

    private CompteRenduSingleton() {
        super();
    }

    public final static CompteRenduSingleton getInstance(){

        if (CompteRenduSingleton.instance == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            // Il est TRES important.
            synchronized(CompteRenduSingleton.class) {
                if (CompteRenduSingleton.instance == null) {
                    CompteRenduSingleton.instance = new CompteRenduSingleton();
                }
            }
        }
        return CompteRenduSingleton.instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPracticien_nom() {
        return practicien_nom;
    }

    public void setPracticien_nom(String practicien_nom) {
        this.practicien_nom = practicien_nom;
    }

    public List<Medicaments> getMedicamentsPresente_rapport() {
        return medicamentsPresente_rapport;
    }

    public void setMedicamentsPresente_rapport(List<Medicaments> medicamentsPresente_rapport) {
        this.medicamentsPresente_rapport = medicamentsPresente_rapport;
    }

    public String getDate_rapport() {
        return date_rapport;
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

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public int getVisiteur_rapport_id() {
        return visiteur_rapport_id;
    }

    public void setVisiteur_rapport_id(int visiteur_rapport_id) {
        this.visiteur_rapport_id = visiteur_rapport_id;
    }
}
