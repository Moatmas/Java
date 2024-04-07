package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private VehicleDao vehicleDao;

    @Autowired
    public VehicleService(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public long create(Vehicle vehicle) throws ServiceException {
        if (vehicle.getNb_place() > 9 || vehicle.getNb_place() < 2) {
            throw new ServiceException("le nombre de places doit être compris entre 2 et 9.", null);
        }
        try {
            return vehicleDao.create(vehicle);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public Vehicle findById(long id) throws ServiceException {
        try {
            return vehicleDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public List<Vehicle> findAll() throws ServiceException {
        try {
            return vehicleDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public long delete(Vehicle vehicle) throws ServiceException {
        try {
            return vehicleDao.delete(vehicle);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public int count() throws ServiceException {
        try {
            return vehicleDao.count();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void modifierVehicle(Vehicle vehicle) throws ServiceException {
        try {
            if (vehicle.getNb_place() > 9 || vehicle.getNb_place() < 2) {
                throw new ServiceException("le nombre de places doit être compris entre 2 et 9.", null);
            }
            vehicleDao.modifier(vehicle);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la modification du client", e);
        }
    }

}