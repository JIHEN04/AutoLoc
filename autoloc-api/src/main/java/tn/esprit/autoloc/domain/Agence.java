package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 50)
    private String ville;

    @Column(length = 255)
    private String adresse;

    @Column(length = 20)
    private String telephone;

    /**
     * 1 Agence -> N Vehicule (cote inverse, LAZY).
     * Volontairement SANS cascade de suppression : supprimer une agence ne supprime pas ses vehicules.
     */
    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Vehicule> vehicules = new ArrayList<>();

    /** 1 Agence -> N Employe (cote inverse, LAZY). */
    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Employe> employes = new ArrayList<>();
}
