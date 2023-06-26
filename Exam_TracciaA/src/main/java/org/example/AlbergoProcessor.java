package org.example;

import java.util.List;

public class AlbergoProcessor {

    private AlbergoProcessor() {
        // Private constructor to prevent instantiation
    }

    public static String processRequest(String request) {
        List<Albergo> alberghi = Albergo.getHotelList();

        if (request.equals("all")) {
            return Albergo.getAllHotelsJSON(alberghi);
        } else if (request.equals("all_sorted")) {
            return Albergo.getSortedHotelsJSON(alberghi);
        } else if (request.equals("most_expensive_suite")) {
            return Albergo.getMostExpensiveSuiteJSON(alberghi);
        } else {
            return "Comando non valido";
        }
    }
}
