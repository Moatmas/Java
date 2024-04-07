package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/modifier")
public class ClientModifierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Autowired
    ClientService clientService;
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
            Client client = clientService.findById(id);
            request.setAttribute("client", client);
            request.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String last_name = request.getParameter("last_name");
            String first_name = request.getParameter("first_name");

            Client client = new Client();

            client.setID((int) id);
            client.setNom(last_name);
            client.setPrenom(first_name);
            clientService.modifierClient(client);

            response.sendRedirect(request.getContextPath() + "/users");
        } catch (ServiceException e) {
            e.printStackTrace();

            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/users/modifier?id=" + id);
        }
    }
}