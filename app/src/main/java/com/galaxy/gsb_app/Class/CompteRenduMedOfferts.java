package com.galaxy.gsb_app.Class;

/**
 * Created by Mapotofu on 18/04/2017.
 */

public class CompteRenduMedOfferts {

    private Medicaments medicament;
    private int Quantity;

    public CompteRenduMedOfferts(Medicaments medicament, int quantity) {
        this.medicament = medicament;
        Quantity = quantity;
    }

    public CompteRenduMedOfferts() {
    }

    public Medicaments getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicaments medicament) {
        this.medicament = medicament;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
