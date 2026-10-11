package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Client;

import java.util.List;

public interface IClientService {

    List<Client> retrieveAllClients();

    Client retrieveClient(Long idClient);

    Client addClient(Client client);

    Client updateClient(Client client);

    void removeClient(Long idClient);
}
