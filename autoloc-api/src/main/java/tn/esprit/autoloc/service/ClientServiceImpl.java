package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> retrieveAllClients() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client retrieveClient(Long idClient) {
        return clientRepository.findById(idClient)
                .orElseThrow(() -> new ResourceNotFoundException("Client", idClient));
    }

    @Override
    @Transactional
    public Client addClient(Client client) {
        client.setIdClient(null); // l'identifiant est genere par la base (IDENTITY)
        if (client.getDateInscription() == null) {
            client.setDateInscription(LocalDate.now());
        }
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Client updateClient(Long idClient, Client client) {
        Client existant = retrieveClient(idClient);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());
        return clientRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeClient(Long idClient) {
        Client existant = retrieveClient(idClient);
        clientRepository.delete(existant);
    }
}
