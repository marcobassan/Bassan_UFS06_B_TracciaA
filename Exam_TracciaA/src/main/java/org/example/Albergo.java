package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Albergo {
    private int id;
    private String name;
    private String description;
    private double price;
    private boolean suite;

    public Albergo(int id, String name, String description, double price, boolean suite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.suite = suite;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSuite() {
        return suite;
    }

    public String getName() {
        return name;
    }

    public static List<Albergo> hotelList() {
        List<Albergo> alberghi = new ArrayList<>();
        alberghi.add(new Albergo(3, "Gran Majestic", "L'Hotel...", 2500.94, true));
        alberghi.add(new Albergo(34, "Albergo dei Re", "Sulle colline toscane...", 303.0, false));
        alberghi.add(new Albergo(17, "Hotel Sole", "...", 4500, true));
        alberghi.add(new Albergo(45, "Gran Hotel", "...", 150, false));
        return alberghi;
    }

    public static String allHotels(List<Albergo> hotels) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(hotels);
    }

    public static String sortedHotels(List<Albergo> hotels) {
        List<Albergo> sortedHotels = new ArrayList<>(hotels);
        sortedHotels.sort(Comparator.comparing(Albergo::getName));
        return allHotels(sortedHotels);
    }

    public static String mostExpensive(List<Albergo> hotels) {
        List<Albergo> suites = new ArrayList<>();
        for (Albergo hotel : hotels) {
            if (hotel.isSuite() && hotel.getPrice() > 0) {
                suites.add(hotel);
            }
        }
        suites.sort(Comparator.comparingDouble(Albergo::getPrice).reversed());
        List<Albergo> mostExpensiveSuite = new ArrayList<>();
        if (!suites.isEmpty()) {
            mostExpensiveSuite.add(suites.get(0));
        }
        return allHotels(mostExpensiveSuite);
    }
}
