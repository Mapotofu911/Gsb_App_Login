package com.galaxy.gsb_app;

/**
 * Created by Mapotofu on 14/02/2017.
 */

public class Region {

    private int id;
    private String Libelle;

    public Region(){

    }

    public Region(int id, String libelle) {
        this.id = id;
        Libelle = libelle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String libelle) {
        Libelle = libelle;
    }
}
