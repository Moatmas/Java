package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
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

@WebServlet("/rents/modifier")
public class ReservationModifierServlet extends HttpServlet {
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

    private long id;
    private int client_id;
    private int vehicle_id;
    private Reservation reservation;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            id = Long.parseLong(request.getParameter("id"));
            reservation= reservationService.getReservationById((int) id);
            client_id = reservation.getClient_id();
            vehicle_id =reservation.getVehicle_id();
            System.out.println(reservation);
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate debut = LocalDate.parse(request.getParameter("begin"), dateFormatter);
            LocalDate fin = LocalDate.parse(request.getParameter("end"), dateFormatter);


            Reservation reservation2 = new Reservation();
            reservation2.setID((int) id);
            reservation2.setDebut(debut);
            reservation2.setFin(fin);
            reservation2.setClient_id(client_id);
            reservation2.setVehicle_id(vehicle_id);
            System.out.println(reservation2);

            reservationService.modifierReservation(reservation2 , reservation);

            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (ServiceException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error_date", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/rents/modifier?id=" + id);
        }
    }
}