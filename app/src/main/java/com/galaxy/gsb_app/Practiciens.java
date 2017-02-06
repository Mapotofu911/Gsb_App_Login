package com.galaxy.gsb_app;

/**
 * Created by Mapotofu on 27/01/2017.
 */

public class Practiciens {

    private int id;
    private String Nom;
    private String Prenom;
    private String Adresse;
    private String CodePostal;
    private String CoeffNotoriete;
    private String Type;
    private String Tel;

    public Practiciens(){}

    public Practiciens(int id, String Nom, String Prenom, String Adresse, String CodePostal, String CoeffNotoriete, String Type, String Tel){
        this.id = id;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Adresse = Adresse;
        this.CodePostal = CodePostal;
        this.CoeffNotoriete = CoeffNotoriete;
        this.Type = Type;
        this.Tel = Tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCoeffNotoriete() {
        return CoeffNotoriete;
    }

    public void setCoeffNotoriete(String coeffNotoriete) {
        CoeffNotoriete = coeffNotoriete;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }
}
