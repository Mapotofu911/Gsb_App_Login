package com.galaxy.gsb_app;

/**
 * Created by Mapotofu on 01/02/2017.
 */

public class Medicaments {

    private int id;
    private String NomMed;
    private String DepotLegale;
    private String NomCommerciale;
    private String Famille;
    private String Prix;
    private String Composition;
    private String Effets;
    private String ContreIndications;

    public Medicaments()
    {

    }

    public Medicaments(int id, String nomMed, String depotLegale, String nomCommerciale, String famille, String prix, String composition, String effets, String contreIndications) {
        this.id = id;
        NomMed = nomMed;
        DepotLegale = depotLegale;
        NomCommerciale = nomCommerciale;
        Famille = famille;
        Prix = prix;
        Composition = composition;
        Effets = effets;
        ContreIndications = contreIndications;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomMed() {
        return NomMed;
    }

    public void setNomMed(String nomMed) {
        NomMed = nomMed;
    }

    public String getDepotLegale() {
        return DepotLegale;
    }

    public void setDepotLegale(String depotLegale) {
        DepotLegale = depotLegale;
    }

    public String getNomCommerciale() {
        return NomCommerciale;
    }

    public void setNomCommerciale(String nomCommerciale) {
        NomCommerciale = nomCommerciale;
    }

    public String getFamille() {
        return Famille;
    }

    public void setFamille(String famille) {
        Famille = famille;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String prix) {
        Prix = prix;
    }

    public String getComposition() {
        return Composition;
    }

    public void setComposition(String composition) {
        Composition = composition;
    }

    public String getEffets() {
        return Effets;
    }

    public void setEffets(String effets) {
        Effets = effets;
    }

    public String getContreIndications() {
        return ContreIndications;
    }

    public void setContreIndications(String contreIndications) {
        ContreIndications = contreIndications;
    }
}
