package com.exemple.projet.service;

import java.util.List;
import java.util.Optional;
import com.exemple.projet.model.Client;

public interface ClientService {
    List<Client> findAll();
    Optional<Client> findById(Integer id);
    Client save(Client client);
    void deleteById(Integer id);
}
