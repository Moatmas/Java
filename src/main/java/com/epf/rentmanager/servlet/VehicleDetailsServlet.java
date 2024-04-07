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

@WebServlet("/cars/details")
public class VehicleDetailsServlet extends HttpServlet {

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
            Vehicle vehicle = vehicleService.findById(id);
            List<Reservation> reservations = reservationService.findResaByVehicleId( (int) id);
            List<Client> clients = new ArrayList<>();
            List<Integer> listIdClient = new ArrayList<>();
            Client clientAAjouter = new Client();
            for( Reservation reservation : reservations) {
                clientAAjouter = clientService.findById(reservation.getClient_id());
                if (!listIdClient.contains(clientAAjouter.getID())){
                    reservation.setClientName(clientAAjouter.getNom(), clientAAjouter.getPrenom());
                    clients.add(clientAAjouter);
                    listIdClient.add(clientAAjouter.getID());
                }
            }

            request.setAttribute("vehicle", vehicle);
            request.setAttribute("reservations", reservations);
            request.setAttribute("client", clients);
            request.setAttribute("nbClients", clients.size());
            request.setAttribute("nbReservations", reservations.size());
            request.getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp").forward(request, response);
        } catch (NumberFormatException | ServiceException e) {
            throw new ServletException(e);
        }
    }

}