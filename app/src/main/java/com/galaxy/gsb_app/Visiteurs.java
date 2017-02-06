package com.galaxy.gsb_app;

/**
 * Created by Mapotofu on 06/02/2017.
 */

public class Visiteurs {

    private int id;
    private String Departement;
    private String Nom;
    private String Prenom;
    private String Adresse;
    private String CodePostal;
    private String Secteur;
    private String Laboratoire;

    public Visiteurs()
    {

    }

    public Visiteurs(int id, String departement, String nom, String prenom, String adresse, String codePostal, String secteur, String laboratoire) {
        this.id = id;
        Departement = departement;
        Nom = nom;
        Prenom = prenom;
        Adresse = adresse;
        CodePostal = codePostal;
        Secteur = secteur;
        Laboratoire = laboratoire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartement() {
        return Departement;
    }

    public void setDepartement(String departement) {
        Departement = departement;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getCodePostal() {
        return CodePostal;
    }

    public void setCodePostal(String codePostal) {
        CodePostal = codePostal;
    }

    public String getSecteur() {
        return Secteur;
    }

    public void setSecteur(String secteur) {
        Secteur = secteur;
    }

    public String getLaboratoire() {
        return Laboratoire;
    }

    public void setLaboratoire(String laboratoire) {
        Laboratoire = laboratoire;
    }
}
