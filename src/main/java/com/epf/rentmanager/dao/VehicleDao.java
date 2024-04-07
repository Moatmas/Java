package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicle;

import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {

	private static VehicleDao instance = null;

	private VehicleDao() {
	}

	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?,?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Reservation WHERE vehicle_id = ?; DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String NOMBRE_VEHICLES_QUERY = "SELECT COUNT(*) AS total_vehicles FROM Vehicle;";
	private static final String MODIFIER_VEHICLES_QUERY = "UPDATE Vehicle SET constructeur = ?, modele = ?, nb_places = ? WHERE id = ?;";

	public long create(Vehicle vehicle) throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, vehicle.getConstructeur());
			ps.setString(2, vehicle.getModele());
			ps.setInt(3, vehicle.getNb_place());

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new DaoException("Échec de la création du véhicule, aucune ligne affectée.", null);
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new DaoException("Échec de la création du véhicule, aucun ID obtenu.", null);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur", e);
		}
	}

	public long delete(Vehicle vehicle) throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(DELETE_VEHICLE_QUERY)) {
			ps.setInt(1, vehicle.getID());
			ps.setInt(2, vehicle.getID());
			ps.execute();
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return vehicle.getID();
	}

	public Vehicle findById(long id) throws DaoException {
		Vehicle vehicle = null;
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(FIND_VEHICLE_QUERY)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String constructeur = rs.getString("constructeur");
					String modele = rs.getString("modele");
					int nb_place = rs.getInt("nb_places");
					vehicle = new Vehicle((int) id, constructeur, modele, nb_place);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return vehicle;
	}

	public List<Vehicle> findAll() throws DaoException {
		List<Vehicle> vehicles = new ArrayList<>();
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(FIND_VEHICLES_QUERY);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int ID = rs.getInt("id");
				String constructeur = rs.getString("constructeur");
				String modele = rs.getString("modele");
				int nb_place = rs.getInt("nb_places");
				Vehicle vehicle = new Vehicle(ID, constructeur, modele, nb_place);
				vehicles.add(vehicle);
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return vehicles;
	}

	public int count() throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(NOMBRE_VEHICLES_QUERY);
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("total_vehicles");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void modifier(Vehicle vehicle) throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(MODIFIER_VEHICLES_QUERY)) {
			ps.setString(1, vehicle.getConstructeur());
			ps.setString(2, vehicle.getModele());
			ps.setInt(3, vehicle.getNb_place());
			ps.setLong(4, vehicle.getID());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la mise à jour du véhicule", e);
		}
	}
}
