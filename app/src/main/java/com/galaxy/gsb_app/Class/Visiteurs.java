package com.galaxy.gsb_app.Class;

/**
 * Created by Mapotofu on 06/02/2017.
 */

public class Visiteurs {

    private int id;
    private String Region;
    private String Nom;
    private String Prenom;
    private String Adresse;
    private String CodePostal;
    private String Secteur;
    private String Laboratoire;
    private String Mail;
    private String Tel;

    public Visiteurs(int id, String region, String nom, String prenom, String adresse, String codePostal, String secteur, String laboratoire) {
        this.id = id;
        Region = region;
        Nom = nom;
        Prenom = prenom;
        Adresse = adresse;
        CodePostal = codePostal;
        Secteur = secteur;
        Laboratoire = laboratoire;
    }

    public Visiteurs()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
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

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }
}
