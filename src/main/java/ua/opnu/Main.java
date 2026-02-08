package ua.opnu;


import ua.opnu.database.Database;
import ua.opnu.service.ClientService;
import ua.opnu.service.DatabaseQueryService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseQueryService queryService = new DatabaseQueryService();

            System.out.println("Max Salary Workers:");
            queryService.findMaxSalaryWorker().forEach(System.out::println);

            System.out.println("\nLongest Projects:");
            queryService.findLongestProject().forEach(System.out::println);

            System.out.println("\nMax Projects Count Client:");
            queryService.findMaxProjectsClient().forEach(System.out::println);

            System.out.println("\n=== Youngest and Eldest Workers ===");
            queryService.findYoungestEldestWorkers().forEach(System.out::println);


            Connection conn = Database.getInstance().getConnection();
            ClientService clientService = new ClientService(conn);

            System.out.println("\n=== Testing ClientService CRUD ===");


            long newClientId = clientService.create("New client");
            System.out.println("Created client with ID: " + newClientId);


            String name = clientService.getById(newClientId);
            System.out.println("Name by ID  " + newClientId + ": " + name);


            clientService.setName(newClientId, "Updated Client");
            System.out.println("Name updated to:: " + clientService.getById(newClientId));


            System.out.println("All clients in the database:");
            clientService.listAll().forEach(System.out::println);


            clientService.deleteById(newClientId);
            System.out.println("Client deleted. List check completed.");

        } catch (Exception e) {
            System.err.println(" Error Occurred" + e.getMessage());
        }
    }
}