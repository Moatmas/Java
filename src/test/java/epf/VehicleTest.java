package epf;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VehicleTest {

    private VehicleDao vehicleDaoMock;
    private VehicleService vehicleService;

    @BeforeEach
    public void setUp() {
        vehicleDaoMock = Mockito.mock(VehicleDao.class);
        vehicleService = new VehicleService(vehicleDaoMock);
    }

    @Test
    public void should_create_vehicle() throws DaoException, ServiceException {
        Vehicle vehicle = createSampleVehicle();
        when(vehicleDaoMock.create(vehicle)).thenReturn(1L);

        long createdVehicleId = vehicleService.create(vehicle);

        assertEquals(1L, createdVehicleId);
    }

    @Test
    public void should_not_create_vehicle_due_to_invalid_number_of_seats() {
        Vehicle vehicle = createSampleVehicleWithInvalidSeats();
        ServiceException exception = assertThrows(ServiceException.class, () -> vehicleService.create(vehicle));
        assertEquals("le nombre de places doit être compris entre 2 et 9.", exception.getMessage());
    }

    @Test
    public void should_find_vehicle_by_id() throws DaoException, ServiceException {
        Vehicle vehicle = createSampleVehicle();
        when(vehicleDaoMock.findById(1L)).thenReturn(vehicle);

        Vehicle foundVehicle = vehicleService.findById(1L);

        assertEquals(vehicle, foundVehicle);
    }

    @Test
    public void should_find_all_vehicles() throws DaoException, ServiceException {
        List<Vehicle> vehicles = createSampleVehicleList();
        when(vehicleDaoMock.findAll()).thenReturn(vehicles);

        List<Vehicle> foundVehicles = vehicleService.findAll();

        assertEquals(vehicles.size(), foundVehicles.size());
        assertEquals(vehicles.get(0), foundVehicles.get(0));
        assertEquals(vehicles.get(1), foundVehicles.get(1));
    }

    @Test
    public void should_delete_vehicle() throws DaoException, ServiceException {
        Vehicle vehicle = createSampleVehicle();
        when(vehicleDaoMock.delete(vehicle)).thenReturn(1L);

        long deletedVehicleId = vehicleService.delete(vehicle);

        assertEquals(1L, deletedVehicleId);
    }

    @Test
    public void should_count_vehicles() throws DaoException, ServiceException {
        when(vehicleDaoMock.count()).thenReturn(5);

        int vehicleCount = vehicleService.count();

        assertEquals(5, vehicleCount);
    }

    @Test
    public void should_modify_vehicle() throws DaoException, ServiceException {
        Vehicle vehicle = createSampleVehicle();

        vehicleService.modifierVehicle(vehicle);

        verify(vehicleDaoMock, times(1)).modifier(vehicle);
    }

    @Test
    public void should_not_modify_vehicle_due_to_invalid_number_of_seats() throws DaoException {
        Vehicle vehicle = createSampleVehicleWithInvalidSeats();
        when(vehicleDaoMock.findById(vehicle.getID())).thenReturn(vehicle);

        ServiceException exception = assertThrows(ServiceException.class, () -> vehicleService.modifierVehicle(vehicle));
        assertEquals("le nombre de places doit être compris entre 2 et 9.", exception.getMessage());
    }

    private Vehicle createSampleVehicle() {
        return new Vehicle("Toyota", "Corolla", 5);
    }

    private Vehicle createSampleVehicleWithInvalidSeats() {
        Vehicle vehicle = new Vehicle();
        vehicle.setNb_place(10);
        return vehicle;
    }

    private List<Vehicle> createSampleVehicleList() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("Toyota", "Corolla", 5));
        vehicles.add(new Vehicle("Honda", "Civic", 4));
        return vehicles;
    }
}
