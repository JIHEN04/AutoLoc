package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.IClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final IClientService clientService;

    @Operation(summary = "Lister les clients")
    @ApiResponse(responseCode = "200", description = "Liste retournee")
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.retrieveAllClients();
    }

    @Operation(summary = "Recuperer un client par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouve"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @GetMapping("/{idClient}")
    public Client getClient(@PathVariable("idClient") Long idClient) {
        return clientService.retrieveClient(idClient);
    }

    @Operation(summary = "Ajouter un client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client cree"),
            @ApiResponse(responseCode = "409", description = "Donnees invalides ou deja existantes")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client addClient(@RequestBody Client client) {
        return clientService.addClient(client);
    }

    @Operation(summary = "Modifier un client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client modifie"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @PutMapping("/{idClient}")
    public Client updateClient(@PathVariable("idClient") Long idClient, @RequestBody Client client) {
        return clientService.updateClient(idClient, client);
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprime"),
            @ApiResponse(responseCode = "404", description = "Client introuvable"),
            @ApiResponse(responseCode = "409", description = "Suppression impossible : donnees liees")
    })
    @DeleteMapping("/{idClient}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClient(@PathVariable("idClient") Long idClient) {
        clientService.removeClient(idClient);
    }
}
