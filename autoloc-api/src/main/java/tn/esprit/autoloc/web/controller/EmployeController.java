package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
@Tag(name = "Employes", description = "Gestion des employes et mutations entre agences")
public class EmployeController {

    private final IEmployeService employeService;

    @Operation(summary = "Lister les employes")
    @ApiResponse(responseCode = "200", description = "Liste retournee")
    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.retrieveAllEmployes();
    }

    @Operation(summary = "Recuperer un employe par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employe trouve"),
            @ApiResponse(responseCode = "404", description = "Employe introuvable")
    })
    @GetMapping("/{idEmploye}")
    public Employe getEmploye(@PathVariable("idEmploye") Long idEmploye) {
        return employeService.retrieveEmploye(idEmploye);
    }

    @Operation(summary = "Ajouter un employe")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employe cree"),
            @ApiResponse(responseCode = "409", description = "Donnees invalides ou deja existantes")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employe addEmploye(@RequestBody Employe employe) {
        return employeService.addEmploye(employe);
    }

    @Operation(summary = "Modifier un employe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employe modifie"),
            @ApiResponse(responseCode = "404", description = "Employe introuvable")
    })
    @PutMapping("/{idEmploye}")
    public Employe updateEmploye(@PathVariable("idEmploye") Long idEmploye, @RequestBody Employe employe) {
        return employeService.updateEmploye(idEmploye, employe);
    }

    @Operation(summary = "Supprimer un employe")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employe supprime"),
            @ApiResponse(responseCode = "404", description = "Employe introuvable"),
            @ApiResponse(responseCode = "409", description = "Suppression impossible : donnees liees")
    })
    @DeleteMapping("/{idEmploye}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmploye(@PathVariable("idEmploye") Long idEmploye) {
        employeService.removeEmploye(idEmploye);
    }

    // ===================== Affectation / desaffectation =====================

    @Operation(summary = "Desaffecter un employe de son agence",
            description = "Scenario 1 : avant une mutation, l'employe est retire de son agence actuelle.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employe desaffecte"),
            @ApiResponse(responseCode = "404", description = "Employe introuvable"),
            @ApiResponse(responseCode = "409", description = "L'employe n'est affecte a aucune agence")
    })
    @PutMapping("/{idEmploye}/desaffecter")
    public Employe desaffecterEmploye(@PathVariable("idEmploye") Long idEmploye) {
        return employeService.desaffecterEmploye(idEmploye);
    }

    @Operation(summary = "Affecter un employe a une agence",
            description = "Scenario 1 : le service RH mute un agent vers l'agence de Tunis. "
                    + "Un employe n'appartient qu'a une seule agence : il doit etre desaffecte avant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employe affecte a l'agence"),
            @ApiResponse(responseCode = "404", description = "Employe ou agence introuvable"),
            @ApiResponse(responseCode = "409", description = "L'employe appartient deja a une autre agence")
    })
    @PutMapping("/{idEmploye}/affecter/{idAgence}")
    public Employe affecterEmploye(@PathVariable("idEmploye") Long idEmploye,
                                   @PathVariable("idAgence") Long idAgence) {
        return employeService.affecterEmploye(idEmploye, idAgence);
    }
}
