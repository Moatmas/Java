package com.epf.rentmanager.servlet;


import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
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

@WebServlet("/cars/modifier")
public class VehicleModifierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private long id;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            id = Long.parseLong(request.getParameter("id"));
            Vehicle vehicle = vehicleService.findById(id);
            request.setAttribute("vehicle", vehicle);
            request.getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String manufacturer = request.getParameter("manufacturer");
            String model = request.getParameter("modele");
            int seats = Integer.parseInt(request.getParameter("seats"));

            Vehicle newVehicle = new Vehicle();


            newVehicle.setID((int) id);
            newVehicle.setConstructeur(manufacturer);
            newVehicle.setModele(model);
            newVehicle.setNb_place(seats);

            vehicleService.modifierVehicle(newVehicle);

            response.sendRedirect(request.getContextPath() + "/cars");
        } catch (ServiceException e) {
            e.printStackTrace();

            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cars/modifier?id=" + id);
        }
    }
}