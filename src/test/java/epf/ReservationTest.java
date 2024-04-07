package epf;

import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.DaoException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ReservationTest {

    @Test
    public void should_not_create_reservation_due_to_invalid_start_date() {
        ReservationDao reservationDaoMock = Mockito.mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setDebut(LocalDate.now().minusDays(1));
        ServiceException exception = assertThrows(ServiceException.class, () -> reservationService.create(reservation));
        assert("La date de début de la réservation ne peut pas être antérieure à aujourd'hui.".equals(exception.getMessage()));
    }

    @Test
    public void should_not_create_reservation_due_to_end_date_before_start_date() {
        ReservationDao reservationDaoMock = Mockito.mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setDebut(LocalDate.now());
        reservation.setFin(LocalDate.now().minusDays(1));
        ServiceException exception = assertThrows(ServiceException.class, () -> reservationService.create(reservation));
        assert("La date de fin de la réservation ne peut pas être antérieure à la date de début.".equals(exception.getMessage()));
    }

    @Test
    public void should_not_create_reservation_due_to_existing_vehicle_reservation() throws DaoException {
        ReservationDao reservationDaoMock = Mockito.mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setVehicle_id(1);
        reservation.setDebut(LocalDate.now().plusDays(1));
        reservation.setFin(LocalDate.now().plusDays(2));
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(new Reservation());
        when(reservationDaoMock.findByVehicleIdAndDate(1, reservation.getDebut(), reservation.getFin())).thenReturn(existingReservations);
        ServiceException exception = assertThrows(ServiceException.class, () -> reservationService.create(reservation));
        assert("La voiture est déjà reservée pour les dates séléctionnées.".equals(exception.getMessage()));
    }

    @Test
    public void should_not_create_reservation_due_to_user_exceeding_maximum_reservation_days() throws DaoException {
        ReservationDao reservationDaoMock = Mockito.mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setClient_id(1);
        reservation.setVehicle_id(1);
        reservation.setDebut(LocalDate.now().plusDays(1));
        reservation.setFin(LocalDate.now().plusDays(8));

        List<Reservation> userReservations = new ArrayList<>();
        Reservation validReservation = new Reservation();
        validReservation.setDebut(LocalDate.now().plusDays(1));
        validReservation.setFin(LocalDate.now().plusDays(3));
        userReservations.add(validReservation);

        when(reservationDaoMock.findByClientIdAndVehicleId(1, 1)).thenReturn(userReservations);
        ServiceException exception = assertThrows(ServiceException.class, () -> reservationService.create(reservation));
        assertEquals("La voiture ne peut pas être réservée plus de 7 jours de suite par le même utilisateur.", exception.getMessage());
    }

}