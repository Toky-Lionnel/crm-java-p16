package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "parametre")
public class Parametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nom", nullable = false, length = 255)
    @NotBlank(message = "Nom is required")
    @Size(max = 255, message = "Nom must be less than or equal to 255 characters")
    private String nom;

    @Column(name = "valeur", nullable = false, length = 255)
    @NotBlank(message = "Valeur is required")
    @Size(max = 255, message = "Valeur must be less than or equal to 255 characters")
    private String valeur;

    @Column(name = "type", nullable = false, length = 50)
    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type must be less than or equal to 50 characters")
    private String type;

    public Parametre() {
    }

    public Parametre(String nom, String valeur, String type) {
        this.nom = nom;
        this.valeur = valeur;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
