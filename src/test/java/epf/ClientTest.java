package epf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.DaoException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientTest {

    @Test
    public void should_return_list_of_clients() throws DaoException {
        ClientDao clientDaoMock = Mockito.mock(ClientDao.class);
        ClientService clientService = new ClientService(clientDaoMock);

        List<Client> clients = createSampleClientList();
        when(clientDaoMock.findAll()).thenReturn(clients);

        List<Client> returnedClients = getClientListFromService(clientService);

        assertIterableEquals(clients, returnedClients);
    }

    @Test
    public void should_create_client() throws ServiceException, DaoException {
        ClientDao clientDaoMock = Mockito.mock(ClientDao.class);
        ClientService clientService = new ClientService(clientDaoMock);
        Client client = createSampleClient();

        mockClientCreation(clientDaoMock, client);

        long clientId = clientService.create(client);

        assertEquals(1L, clientId);
    }

    @Test
    public void should_not_create_client_due_to_short_name() {
        ClientDao clientDaoMock = Mockito.mock(ClientDao.class);
        ClientService clientService = new ClientService(clientDaoMock);
        Client client = new Client("Do", "John", "john@example.com", LocalDate.of(1990, 1, 1));

        ServiceException exception = assertThrows(ServiceException.class, () -> clientService.create(client));
        assertEquals("Le nom du client doit faire au moins 3 caractères.", exception.getMessage());
    }

    @Test
    public void should_not_create_client_due_to_minor() {
        ClientDao clientDaoMock = Mockito.mock(ClientDao.class);
        ClientService clientService = new ClientService(clientDaoMock);
        Client client = new Client("Doe", "John", "john@example.com", LocalDate.now().minusYears(17));

        ServiceException exception = assertThrows(ServiceException.class, () -> clientService.create(client));
        assertEquals("Le client doit avoir au moins 18 ans.", exception.getMessage());
    }

    @Test
    public void should_create_first_client_and_not_create_second_due_to_existing_email() throws DaoException, ServiceException {
        ClientDao clientDaoMock = Mockito.mock(ClientDao.class);
        ClientService clientService = new ClientService(clientDaoMock);
        Client client1 = new Client("Doe", "John", "john@example.com", LocalDate.of(1990, 1, 1));
        Client client2 = new Client("Smith", "Jane", "john@example.com", LocalDate.of(1985, 5, 15));

        mockClientCreation(clientDaoMock, client1);
        when(clientDaoMock.emailExists(client2.getEmail())).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> clientService.create(client2));
        assertEquals("Cette adresse mail existe déjà", exception.getMessage());
    }

    private List<Client> createSampleClientList() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("Doe", "John", "john@example.com", LocalDate.of(1990, 1, 1)));
        clients.add(new Client("Smith", "Jane", "jane@example.com", LocalDate.of(1985, 5, 15)));
        return clients;
    }

    private List<Client> getClientListFromService(ClientService clientService) {
        try {
            return clientService.findAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private Client createSampleClient() {
        return new Client("Doe", "John", "john@example.com", LocalDate.of(1990, 1, 1));
    }

    private void mockClientCreation(ClientDao clientDaoMock, Client client) throws DaoException {
        when(clientDaoMock.emailExists(client.getEmail())).thenReturn(false);
        when(clientDaoMock.create(client)).thenReturn(1L);
    }
}
