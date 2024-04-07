package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	private ClientDao clientDao;

	@Autowired
	public ClientService(ClientDao clientDao) {
		this.clientDao = clientDao;
	}


	public long create(Client client) throws ServiceException {
		try {

			if (client.getNom().length() < 3) {
				throw new ServiceException("Le nom du client doit faire au moins 3 caractères.", null);
			}
			if (client.getPrenom().length() < 3) {
				throw new ServiceException("Le prénom du client doit faire au moins 3 caractères.", null);
			}
			if (clientDao.emailExists(client.getEmail())) {
				throw new ServiceException("Cette adresse mail existe déjà", null);
			}
			client.setNom(client.getNom().toUpperCase());
			LocalDate dateActuelle = LocalDate.now();
			int age = Period.between(client.getNaissance(), dateActuelle).getYears();
			if (age < 18) {
				throw new ServiceException("Le client doit avoir au moins 18 ans.", null);
			}
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la création du client.", e);
		}
	}

	public Client findById(long id) throws ServiceException {
		try {
			return clientDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la recherche par ID", e);
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la recherche ", e);
		}
	}

	public long delete(Client client) throws ServiceException {
		try {
			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la supression ", e);
		}
	}

	public int count() throws ServiceException {
		try {
			return clientDao.count();
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void modifierClient(Client client) throws ServiceException {
		try {
			if (client.getNom().length() < 3) {
				throw new ServiceException("Le nom du client doit faire au moins 3 caractères.", null);
			}
			if (client.getPrenom().length() < 3) {
				throw new ServiceException("Le prénom du client doit faire au moins 3 caractères.", null);
			}
			client.setNom(client.getNom().toUpperCase());
			clientDao.modifier(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la modification du client", e);
		}
	}

}