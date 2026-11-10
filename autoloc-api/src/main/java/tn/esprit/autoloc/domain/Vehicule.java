package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @Column(nullable = false, unique = true, length = 20)
    private String immatriculation;

    @Column(nullable = false, length = 50)
    private String marque;

    @Column(nullable = false, length = 50)
    private String modele;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategorieVehicule categorie;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutVehicule statut;

    /** N Vehicule -> 1 Agence (cote proprietaire : porte la cle etrangere agence_id). */
    @ManyToOne
    @JoinColumn(name = "agence_id")
    private Agence agence;

    /**
     * N Vehicule <-> N Equipement (cote proprietaire, table de jointure vehicule_equipement).
     * Ex. : GPS, siege bebe, coffre de toit.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "vehicule_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id"))
    private Set<Equipement> equipements = new HashSet<>();

    /** 1 Vehicule -> N Reservation (cote inverse). */
    @OneToMany(mappedBy = "vehicule")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    /** 1 Vehicule -> N Maintenance (cote inverse). */
    @OneToMany(mappedBy = "vehicule")
    @JsonIgnore
    private List<Maintenance> maintenances = new ArrayList<>();
}
