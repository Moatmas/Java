package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exception.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private static ClientDao instance = null;

	public ClientDao(Connection connection) {
		this.connection = connection;
	}

	private Connection connection;

	private ClientDao() {
	}


	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?)";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Reservation WHERE client_id = ?; DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String NOMBRE_CLIENTS_QUERY = "SELECT COUNT(*) AS total_clients FROM Client;";
	private static final String MODIFIER_CLIENTS_QUERY = "UPDATE client SET nom = ?, prenom = ? WHERE id = ?;";

	public long create(Client client) throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, java.sql.Date.valueOf(client.getNaissance()));

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new DaoException("Échec de la création du client, aucune ligne affectée.", null);
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new DaoException("Échec de la création du client, aucun ID obtenu.", null);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur", e);
		}
	}

	public long delete(Client client) throws DaoException {
		try {
			Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			PreparedStatement ps = connexion.prepareStatement(DELETE_CLIENT_QUERY);
			ps.setInt(1, client.getID());
			ps.setInt(2, client.getID());

			ps.execute();
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return client.getID();
	}

	public Client findById(long id) throws DaoException {
		Client client = null;
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(FIND_CLIENT_QUERY)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String nom = rs.getString("nom");
					String prenom = rs.getString("prenom");
					String mail = rs.getString("email");
					LocalDate naissance = rs.getDate("naissance").toLocalDate();
					client = new Client((int) id, nom, prenom, mail, naissance);
				}
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		}
		return client;
	}


	public List<Client> findAll() throws DaoException {
		List<Client> clients = new ArrayList<>();
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(FIND_CLIENTS_QUERY);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int ID = rs.getInt("id");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String email = rs.getString("email");
				LocalDate naissance = rs.getDate("naissance").toLocalDate();
				Client client = new Client(ID, nom, prenom, email, naissance);
				clients.add(client);
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche de tous les clients.", e);
		}
		return clients;

	}

	public boolean emailExists(String email) throws DaoException {
		List<Client> clients = findAll();
		for (Client client : clients) {
			if (client.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}

	public int count() throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(NOMBRE_CLIENTS_QUERY);
			 ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt("total_clients");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void modifier(Client client) throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(MODIFIER_CLIENTS_QUERY)) {
			;
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setLong(3, client.getID());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la mise à jour du client", e);
		}
	}
}
