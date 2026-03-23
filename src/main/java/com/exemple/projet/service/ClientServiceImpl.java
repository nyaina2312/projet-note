package com.exemple.projet.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.exemple.projet.model.Client;
import com.exemple.projet.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    
    @Override
    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }
    
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
    
    @Override
    public void deleteById(Integer id) {
        clientRepository.deleteById(id);
    }
}
