package com.epf.rentmanager.model;

public class Vehicle {
    private String constructeur;
    private String modele;
    private int nb_place;
    private int ID;

    public Vehicle(int ID, String constructeur, String modele, int nb_place) {
        this.constructeur = constructeur;
        this.modele = modele;
        this.nb_place = nb_place;
        this.ID = ID;
    }

    public Vehicle(String constructeur, String modele, int nb_place) {
        this.constructeur = constructeur;
        this.modele = modele;
        this.nb_place = nb_place;
    }

    public Vehicle() {

    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "constructeur=" + constructeur +
                ", modele=" + modele +
                ", nb_place=" + nb_place +
                ", ID=" + ID +
                '}';
    }

    public String getConstructeur() {
        return constructeur;
    }

    public void setConstructeur(String constructeur) {
        this.constructeur = constructeur;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public void setNb_place(int nb_place) {
        this.nb_place = nb_place;
    }

    public int getNb_place() {
        return nb_place;
    }

    public String getModele() {
        return modele;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}