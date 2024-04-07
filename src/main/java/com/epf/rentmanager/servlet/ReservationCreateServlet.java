package com.epf.rentmanager.servlet;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.epf.rentmanager.utils.IOUtils.print;
@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ClientService clientService;
    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Reservation reservation = null;
        List<Client> clients = null;
        try {
            clients = clientService.findAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("clients", clients);

        List<Vehicle> vehicles = null;
        try {
            vehicles = vehicleService.findAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("cars", vehicles);

        request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);

    }
    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int vehicle_id = Integer.parseInt(request.getParameter("car"));
            int client_id = Integer.parseInt(request.getParameter("client"));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate debut = LocalDate.parse(request.getParameter("begin"), dateFormatter);
            LocalDate fin = LocalDate.parse(request.getParameter("end"), dateFormatter);

            Reservation newReservation = new Reservation(client_id, vehicle_id , debut, fin);

            reservationService.create(newReservation);

            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (NumberFormatException | ServiceException e) {

            e.printStackTrace();
            if (e.getMessage().equals("La voiture est déjà reservée pour les dates séléctionnées.")) {
                request.setAttribute("error_date", e.getMessage());
            }
            if (e.getMessage().equals("La voiture ne peut pas être réservée plus de 7 jours de suite par le même utilisateur.")) {
                request.setAttribute("error_date2", e.getMessage());
            }
            if (e.getMessage().equals("La voiture ne peut pas être réservée 30 jours de suite sans pause.")) {
                request.setAttribute("error_date3", e.getMessage());
            }

            List<Vehicle> cars = null;
            try {
                cars = vehicleService.findAll();
            } catch (ServiceException ex) {
                throw new RuntimeException(ex);
            }
            List<Client> clients = null;
            try {
                clients = clientService.findAll();
            } catch (ServiceException ex) {
                throw new RuntimeException(ex);
            }

            request.setAttribute("cars", cars);
            request.setAttribute("clients", clients);

            this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request,response);

        }
    }
}