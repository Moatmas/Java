package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);

    }
    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String manufacturer = request.getParameter("manufacturer");
            String model = request.getParameter("modele");
            int seats = Integer.parseInt(request.getParameter("seats"));

            Vehicle newVehicle = new Vehicle(manufacturer, model, seats);

            vehicleService.create(newVehicle);
            response.sendRedirect(request.getContextPath() + "/cars");
        } catch (NumberFormatException | ServiceException e) {

            e.printStackTrace();
            if (e.getMessage().equals("le nombre de places doit être compris entre 2 et 9.")) {
                request.setAttribute("error_place", e.getMessage());
            }
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request,response);
        }
    }
}