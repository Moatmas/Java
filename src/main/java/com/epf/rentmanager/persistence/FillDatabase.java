package com.epf.rentmanager.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.DeleteDbFiles;

import com.epf.rentmanager.persistence.ConnectionManager;

public class FillDatabase {


    public static void main(String[] args) throws Exception {
        try {
            DeleteDbFiles.execute("~", "RentManagerDatabase", true);
            insertWithPreparedStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertWithPreparedStatement() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement createPreparedStatement = null;

        List<String> createTablesQueries = new ArrayList<>();
        createTablesQueries.add("CREATE TABLE IF NOT EXISTS Client(id INT primary key auto_increment, client_id INT, nom VARCHAR(100), prenom VARCHAR(100), email VARCHAR(100), naissance DATETIME)");
        createTablesQueries.add("CREATE TABLE IF NOT EXISTS Vehicle(id INT primary key auto_increment, constructeur VARCHAR(100), modele VARCHAR(100), nb_places TINYINT(255))");
        createTablesQueries.add("CREATE TABLE IF NOT EXISTS Reservation(id INT primary key auto_increment, client_id INT, foreign key(client_id) REFERENCES Client(id), vehicle_id INT, foreign key(vehicle_id) REFERENCES Vehicle(id), debut DATETIME, fin DATETIME)");

        try {
            connection.setAutoCommit(false);

            for (String createQuery : createTablesQueries) {
                createPreparedStatement = connection.prepareStatement(createQuery);
                createPreparedStatement.executeUpdate();
                createPreparedStatement.close();
            }

            Statement stmt = connection.createStatement();
            stmt.execute("INSERT INTO Vehicle(constructeur,modele, nb_places) VALUES('Toyota', 'Corolla', 5)");
            stmt.execute("INSERT INTO Vehicle(constructeur,modele, nb_places) VALUES('Ford','Focus', 5)");
            stmt.execute("INSERT INTO Vehicle(constructeur,modele, nb_places) VALUES('Volkswagen','Golf', 5)");
            stmt.execute("INSERT INTO Vehicle(constructeur,modele, nb_places) VALUES('Honda','Civic', 5)");

            stmt.execute("INSERT INTO Client(nom, prenom, email, naissance) VALUES('Smith', 'John', 'john.smith@email.com', '1985-03-15')");
            stmt.execute("INSERT INTO Client(nom, prenom, email, naissance) VALUES('Doe', 'Jane', 'jane.doe@email.com', '1987-07-10')");
            stmt.execute("INSERT INTO Client(nom, prenom, email, naissance) VALUES('Johnson', 'Michael', 'michael.johnson@email.com', '1990-12-28')");
            stmt.execute("INSERT INTO Client(nom, prenom, email, naissance) VALUES('Williams', 'Emily', 'emily.williams@email.com', '1982-09-04')");

            connection.commit();
            System.out.println("Success!");
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
