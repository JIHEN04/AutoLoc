package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicules", description = "Gestion de la flotte et affectations des vehicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les vehicules")
    @ApiResponse(responseCode = "200", description = "Liste retournee")
    @GetMapping
    public List<Vehicule> getAllVehicules() {
        return vehiculeService.retrieveAllVehicules();
    }

    @Operation(summary = "Recuperer un vehicule par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicule trouve"),
            @ApiResponse(responseCode = "404", description = "Vehicule introuvable")
    })
    @GetMapping("/{idVehicule}")
    public Vehicule getVehicule(@PathVariable("idVehicule") Long idVehicule) {
        return vehiculeService.retrieveVehicule(idVehicule);
    }

    @Operation(summary = "Ajouter un vehicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicule cree"),
            @ApiResponse(responseCode = "409", description = "Donnees invalides ou deja existantes")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule addVehicule(@RequestBody Vehicule vehicule) {
        return vehiculeService.addVehicule(vehicule);
    }

    @Operation(summary = "Modifier un vehicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicule modifie"),
            @ApiResponse(responseCode = "404", description = "Vehicule introuvable")
    })
    @PutMapping("/{idVehicule}")
    public Vehicule updateVehicule(@PathVariable("idVehicule") Long idVehicule, @RequestBody Vehicule vehicule) {
        return vehiculeService.updateVehicule(idVehicule, vehicule);
    }

    @Operation(summary = "Supprimer un vehicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicule supprime"),
            @ApiResponse(responseCode = "404", description = "Vehicule introuvable"),
            @ApiResponse(responseCode = "409", description = "Suppression impossible : donnees liees")
    })
    @DeleteMapping("/{idVehicule}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeVehicule(@PathVariable("idVehicule") Long idVehicule) {
        vehiculeService.removeVehicule(idVehicule);
    }

    // ===================== Affectations =====================

    @Operation(summary = "Affecter un vehicule a une agence",
            description = "Scenario 2 : un vehicule neuf arrive et est rattache a l'agence de Sfax.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicule rattache a l'agence"),
            @ApiResponse(responseCode = "404", description = "Vehicule ou agence introuvable")
    })
    @PutMapping("/{idVehicule}/affecter-agence/{idAgence}")
    public Vehicule affecterVehicule(@PathVariable("idVehicule") Long idVehicule,
                                     @PathVariable("idAgence") Long idAgence) {
        return vehiculeService.affecterVehicule(idVehicule, idAgence);
    }

    @Operation(summary = "Installer un equipement sur un vehicule",
            description = "Scenario 3 : un client demande un siege bebe, l'agent l'installe sur le vehicule.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipement installe"),
            @ApiResponse(responseCode = "404", description = "Vehicule ou equipement introuvable")
    })
    @PutMapping("/{idVehicule}/affecter-equipement/{idEquipement}")
    public Vehicule affecterEquipement(@PathVariable("idVehicule") Long idVehicule,
                                       @PathVariable("idEquipement") Long idEquipement) {
        return vehiculeService.affecterEquipement(idVehicule, idEquipement);
    }

    @Operation(summary = "Retirer un equipement d'un vehicule",
            description = "Scenario 3 : au retour du vehicule, l'agent retire le siege bebe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipement retire"),
            @ApiResponse(responseCode = "404", description = "Vehicule ou equipement introuvable"),
            @ApiResponse(responseCode = "409", description = "Equipement non installe sur ce vehicule")
    })
    @PutMapping("/{idVehicule}/desaffecter-equipement/{idEquipement}")
    public Vehicule desaffecterEquipement(@PathVariable("idVehicule") Long idVehicule,
                                          @PathVariable("idEquipement") Long idEquipement) {
        return vehiculeService.desaffecterEquipement(idVehicule, idEquipement);
    }
}
