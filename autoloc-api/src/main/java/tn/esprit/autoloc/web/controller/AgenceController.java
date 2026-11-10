package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences AutoLoc")
public class AgenceController {

    private final IAgenceService agenceService;

    @Operation(summary = "Lister les agences")
    @ApiResponse(responseCode = "200", description = "Liste retournee")
    @GetMapping
    public List<Agence> getAllAgences() {
        return agenceService.retrieveAllAgences();
    }

    @Operation(summary = "Recuperer une agence par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence trouvee"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @GetMapping("/{idAgence}")
    public Agence getAgence(@PathVariable("idAgence") Long idAgence) {
        return agenceService.retrieveAgence(idAgence);
    }

    @Operation(summary = "Ajouter une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agence creee"),
            @ApiResponse(responseCode = "409", description = "Donnees invalides ou deja existantes")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agence addAgence(@RequestBody Agence agence) {
        return agenceService.addAgence(agence);
    }

    @Operation(summary = "Modifier une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence modifiee"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @PutMapping("/{idAgence}")
    public Agence updateAgence(@PathVariable("idAgence") Long idAgence, @RequestBody Agence agence) {
        return agenceService.updateAgence(idAgence, agence);
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agence supprimee"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable"),
            @ApiResponse(responseCode = "409", description = "Suppression impossible : donnees liees")
    })
    @DeleteMapping("/{idAgence}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAgence(@PathVariable("idAgence") Long idAgence) {
        agenceService.removeAgence(idAgence);
    }
}
