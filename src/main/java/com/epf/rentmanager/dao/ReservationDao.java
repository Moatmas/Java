package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exception.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

	private static ReservationDao instance = null;

	private ReservationDao() {
	}


	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String NOMBRE_RESERVATION_QUERY = "SELECT COUNT(*) AS total_reservations FROM Reservation;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_AND_DATE_QUERY = "SELECT id,client_id, debut, fin FROM Reservation WHERE vehicle_id = ? AND (debut BETWEEN ? AND ? OR fin BETWEEN ? AND ?);";
	private static final String FIND_RESERVATIONS_BY_CLIENT_AND_VEHICLE_QUERY = "SELECT * FROM Reservation WHERE client_id=? AND vehicle_id=?;";
	private static final String MODIFIER_RESERVATION_QUERY = "UPDATE reservation SET debut = ?, fin = ? WHERE id = ?;";

	public long create(Reservation reservation) throws DaoException {
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, reservation.getClient_id());
			ps.setInt(2, reservation.getVehicle_id());
			ps.setDate(3, java.sql.Date.valueOf(reservation.getDebut()));
			ps.setDate(4, java.sql.Date.valueOf(reservation.getFin()));

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new DaoException("Échec de la création de la réservation, aucune ligne affectée.", null);
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new DaoException("Échec de la création de la réservation, aucun ID obtenu.", null);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur", e);
		}
	}

	public long delete(Reservation reservation) throws DaoException {
		try {
			Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			PreparedStatement ps = connexion.prepareStatement(DELETE_RESERVATION_QUERY);
			ps.setInt(1, reservation.getID());
			ps.execute();
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return reservation.getID();
	}

	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {
			ps.setLong(1, clientId);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					int vehicle_id = resultSet.getInt("vehicle_id");
					LocalDate debut = resultSet.getDate("debut").toLocalDate();
					LocalDate fin = resultSet.getDate("fin").toLocalDate();
					Reservation reservation = new Reservation(id, (int) clientId, vehicle_id, debut, fin);
					reservations.add(reservation);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return reservations;
	}

	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {
			ps.setLong(1, vehicleId);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					int clientId = resultSet.getInt("client_id");
					LocalDate debut = resultSet.getDate("debut").toLocalDate();
					LocalDate fin = resultSet.getDate("fin").toLocalDate();
					Reservation reservation = new Reservation(id, clientId, (int) vehicleId, debut, fin);
					reservations.add(reservation);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return reservations;
	}

	public List<Reservation> findAll() throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_QUERY);
			 ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int clientId = resultSet.getInt("client_id");
				int vehicleId = resultSet.getInt("vehicle_id");
				LocalDate debut = resultSet.getDate("debut").toLocalDate();
				LocalDate fin = resultSet.getDate("fin").toLocalDate();
				Reservation reser = new Reservation(id, clientId, vehicleId, debut, fin);
				reservations.add(reser);
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return reservations;
	}

	public int count() throws DaoException {
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(NOMBRE_RESERVATION_QUERY);
			 ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt("total_reservations");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Reservation> findByVehicleIdAndDate(long vehicleId, LocalDate debut, LocalDate fin) throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connexion = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connexion.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_AND_DATE_QUERY)) {
			ps.setLong(1, vehicleId);
			ps.setDate(2, Date.valueOf(debut));
			ps.setDate(3, Date.valueOf(fin));
			ps.setDate(4, Date.valueOf(debut));
			ps.setDate(5, Date.valueOf(fin));
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					int clientId = resultSet.getInt("client_id");
					LocalDate Debut = resultSet.getDate("debut").toLocalDate();
					LocalDate Fin = resultSet.getDate("fin").toLocalDate();
					Reservation reservation = new Reservation(id, clientId, (int) vehicleId, Debut, Fin);
					reservations.add(reservation);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur ", e);
		}
		return reservations;
	}

	public List<Reservation> findByClientIdAndVehicleId(long clientId, long vehicleId) throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_AND_VEHICLE_QUERY)) {
			ps.setLong(1, clientId);
			ps.setLong(2, vehicleId);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					LocalDate debut = resultSet.getDate("debut").toLocalDate();
					LocalDate fin = resultSet.getDate("fin").toLocalDate();
					Reservation reservation = new Reservation(id, (int) clientId, (int) vehicleId, debut, fin);
					reservations.add(reservation);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur", e);
		}
		return reservations;
	}

	public void modifier(Reservation reservation) throws DaoException {
		try (Connection connection = DriverManager.getConnection("jdbc:h2:~/RentManagerDatabase", "", "");
			 PreparedStatement ps = connection.prepareStatement(MODIFIER_RESERVATION_QUERY)) {
			ps.setDate(1, Date.valueOf(reservation.getDebut()));
			ps.setDate(2, Date.valueOf(reservation.getFin()));
			ps.setLong(3, reservation.getID());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la mise à jour de la réservation: " + e.getMessage(), e);
		}
	}

}
