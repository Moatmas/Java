package com.epf.rentmanager.service;


import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.dao.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {


    private ReservationDao reservationDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        try {
            LocalDate today = LocalDate.now();
            if (reservation.getDebut().isBefore(today)) {
                throw new ServiceException("La date de début de la réservation ne peut pas être antérieure à aujourd'hui.", null);
            }
            if (reservation.getFin().isBefore(reservation.getDebut())) {
                throw new ServiceException("La date de fin de la réservation ne peut pas être antérieure à la date de début.", null);
            }
            reservationExistant(reservation);
            userReservation(reservation);
            carReservation(reservation);

            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void reservationExistant(Reservation reservation) throws ServiceException {
        List<Reservation> existingReservation = null;
        try {
            existingReservation = reservationDao.findByVehicleIdAndDate(
                    reservation.getVehicle_id(), reservation.getDebut(), reservation.getFin());
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur.", e);
        }
        if (!existingReservation.isEmpty()) {
            throw new ServiceException("La voiture est déjà reservée pour les dates séléctionnées.", null);
        }
    }

    public void userReservation(Reservation reservation) throws ServiceException {
        List<Reservation> userReservations;
        try {
            userReservations = reservationDao.findByClientIdAndVehicleId(reservation.getClient_id(), reservation.getVehicle_id());
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur.", e);
        }
        userReservations.add(reservation);
        userReservations.sort(Comparator.comparing(Reservation::getDebut));
        long totalReservationDays = 0;
        LocalDate previousEnd = null;
        for (Reservation userReservation : userReservations) {
            if (previousEnd != null && !previousEnd.plusDays(1).equals(userReservation.getDebut())) {
                totalReservationDays = 0;
            }
            totalReservationDays += Period.between(userReservation.getDebut(), userReservation.getFin()).getDays() + 1;
            previousEnd = userReservation.getFin();

            if (totalReservationDays > 7) {
                throw new ServiceException("La voiture ne peut pas être réservée plus de 7 jours de suite par le même utilisateur.", null);
            }
        }
    }

    public void carReservation(Reservation reservation) throws ServiceException {
        List<Reservation> carReservations = null;
        try {
            carReservations = reservationDao.findResaByVehicleId(reservation.getVehicle_id());
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur.", e);
        }
        carReservations.add(reservation);
        carReservations.sort(Comparator.comparing(Reservation::getDebut));
        LocalDate End = null;
        long carReservationDays = 0;
        for (Reservation carReservation : carReservations) {
            if (End != null && !End.plusDays(1).equals(carReservation.getDebut())) {
                carReservationDays = 0;
            }
            carReservationDays += Period.between(carReservation.getDebut(), carReservation.getFin()).getDays() + 1;
            End = carReservation.getFin();

            if (carReservationDays > 30) {
                throw new ServiceException("La voiture ne peut pas être réservée 30 jours de suite sans pause.", null);
            }
        }
    }

    public List<Reservation> findResaByClientId(long id) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public List<Reservation> findResaByVehicleId(long id) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public long delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public Reservation getReservationById(int reservationId) throws ServiceException {
        try {
            List<Reservation> reservations = reservationDao.findAll();

            for (Reservation reservation : reservations) {
                if (reservation.getID() == reservationId) {
                    return reservation;
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return null;
    }

    public int count() throws ServiceException {
        try {
            return reservationDao.count();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void modifierReservation(Reservation reservation, Reservation reservation2) throws ServiceException {
        try {
            LocalDate today = LocalDate.now();
            if (reservation.getDebut().isBefore(today)) {
                throw new ServiceException("La date de début de la réservation ne peut pas être antérieure à aujourd'hui.", null);
            }
            if (reservation.getFin().isBefore(reservation.getDebut())) {
                throw new ServiceException("La date de fin de la réservation ne peut pas être antérieure à la date de début.", null);
            }

            List<Reservation> existingReservation = null;
            try {
                existingReservation = reservationDao.findByVehicleIdAndDate(
                        reservation.getVehicle_id(), reservation.getDebut(), reservation.getFin());
            } catch (DaoException e) {
                throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur.", e);
            }
            existingReservation.removeIf(ancien -> Objects.equals(ancien.getID(), reservation2.getID()));

            if (!existingReservation.isEmpty()) {
                throw new ServiceException("La voiture est déjà reservée pour les dates séléctionnées.", null);
            }

            List<Reservation> userReservations = null;
            try {
                userReservations = reservationDao.findByClientIdAndVehicleId(reservation.getClient_id(), reservation.getVehicle_id());
            } catch (DaoException e) {
                throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur.", e);
            }
            userReservations.removeIf(ancien -> Objects.equals(ancien.getID(), reservation2.getID()));
            userReservations.add(reservation);
            userReservations.sort(Comparator.comparing(Reservation::getDebut));
            long totalReservationDays = 0;
            LocalDate previousEnd = null;
            for (Reservation userReservation : userReservations) {
                if (previousEnd != null && !previousEnd.plusDays(1).equals(userReservation.getDebut())) {
                    totalReservationDays = 0;
                }
                totalReservationDays += Period.between(userReservation.getDebut(), userReservation.getFin()).getDays() + 1;
                previousEnd = userReservation.getFin();

                if (totalReservationDays > 7) {
                    throw new ServiceException("La voiture ne peut pas être réservée plus de 7 jours de suite par le même utilisateur.", null);
                }
            }

            List<Reservation> carReservations = null;
            try {
                carReservations = reservationDao.findResaByVehicleId(reservation.getVehicle_id());
            } catch (DaoException e) {
                throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur.", e);
            }
            carReservations.removeIf(ancien -> Objects.equals(ancien.getID(), reservation2.getID()));
            carReservations.add(reservation);
            carReservations.sort(Comparator.comparing(Reservation::getDebut));
            LocalDate End = null;
            long carReservationDays = 0;
            for (Reservation carReservation : carReservations) {
                if (End != null && !End.plusDays(1).equals(carReservation.getDebut())) {
                    carReservationDays = 0;
                }
                carReservationDays += Period.between(carReservation.getDebut(), carReservation.getFin()).getDays() + 1;
                End = carReservation.getFin();

                if (carReservationDays > 30) {
                    throw new ServiceException("La voiture ne peut pas être réservée 30 jours de suite sans pause.", null);
                }
            }

            reservationDao.modifier(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}