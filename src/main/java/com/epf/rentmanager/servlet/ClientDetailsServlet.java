package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/details")
public class ClientDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @Autowired
    ClientService clientService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    VehicleService vehicleService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Client client = clientService.findById(id);
            List<Reservation> reservations = reservationService.findResaByClientId( (int) id);
            List<Vehicle> vehicles = new ArrayList<>();
            List<Integer> listIdVehicle = new ArrayList<>();
            Vehicle vehicleAAjouter = new Vehicle();
            for( Reservation reservation : reservations) {
                vehicleAAjouter = vehicleService.findById(reservation.getVehicle_id());
                if (!listIdVehicle.contains(vehicleAAjouter.getID())){
                    reservation.setVehicleName(vehicleAAjouter.getConstructeur(), vehicleAAjouter.getModele());
                    vehicles.add(vehicleAAjouter);
                    listIdVehicle.add(vehicleAAjouter.getID());
                }
            }

            request.setAttribute("client", client);
            request.setAttribute("reservations", reservations);
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("nbVehicles", vehicles.size());
            request.setAttribute("nbReservations", reservations.size());
            request.getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
        } catch (NumberFormatException | ServiceException e) {
            throw new ServletException(e);
        }
    }

}