package org.example;

import java.util.List;

public class AlbergoProcessor {

    private AlbergoProcessor() {
    }

    public static String Request(String request) {
        List<Albergo> alberghi = Albergo.hotelList();

        if (request.equals("all")) {
            return Albergo.allHotels(alberghi);
        } else if (request.equals("all_sorted")) {
            return Albergo.sortedHotels(alberghi);
        } else if (request.equals("most_expensive")) {
            String mostExpensive = Albergo.mostExpensive(alberghi);
            return mostExpensive;
        } else {
            return "Comando non valido";
        }
    }
}
