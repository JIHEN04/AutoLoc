package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
@Tag(name = "Equipements", description = "Gestion des equipements optionnels (GPS, siege bebe...)")
public class EquipementController {

    private final IEquipementService equipementService;

    @Operation(summary = "Lister les equipements")
    @ApiResponse(responseCode = "200", description = "Liste retournee")
    @GetMapping
    public List<Equipement> getAllEquipements() {
        return equipementService.retrieveAllEquipements();
    }

    @Operation(summary = "Recuperer un equipement par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipement trouve"),
            @ApiResponse(responseCode = "404", description = "Equipement introuvable")
    })
    @GetMapping("/{idEquipement}")
    public Equipement getEquipement(@PathVariable("idEquipement") Long idEquipement) {
        return equipementService.retrieveEquipement(idEquipement);
    }

    @Operation(summary = "Ajouter un equipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipement cree"),
            @ApiResponse(responseCode = "409", description = "Donnees invalides ou deja existantes")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipement addEquipement(@RequestBody Equipement equipement) {
        return equipementService.addEquipement(equipement);
    }

    @Operation(summary = "Modifier un equipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipement modifie"),
            @ApiResponse(responseCode = "404", description = "Equipement introuvable")
    })
    @PutMapping("/{idEquipement}")
    public Equipement updateEquipement(@PathVariable("idEquipement") Long idEquipement, @RequestBody Equipement equipement) {
        return equipementService.updateEquipement(idEquipement, equipement);
    }

    @Operation(summary = "Supprimer un equipement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Equipement supprime"),
            @ApiResponse(responseCode = "404", description = "Equipement introuvable"),
            @ApiResponse(responseCode = "409", description = "Suppression impossible : donnees liees")
    })
    @DeleteMapping("/{idEquipement}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEquipement(@PathVariable("idEquipement") Long idEquipement) {
        equipementService.removeEquipement(idEquipement);
    }
}
