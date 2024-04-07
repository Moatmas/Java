package com.epf.rentmanager.model;

import java.time.LocalDate;


public class Client {
    private int ID;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate naissance;

    public Client(int ID, String nom, String prenom, String email, LocalDate naissance) {
        this.ID = ID;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.naissance = naissance;
    }

    public Client(String nom, String prenom, String email, LocalDate naissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.naissance = naissance;
    }

    public Client() {

    }

    @Override
    public String toString() {
        return "Client{" +
                "ID=" + ID +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", naissance=" + naissance +
                '}';
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getID() {
        return ID;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getNaissance() {
        return naissance;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}