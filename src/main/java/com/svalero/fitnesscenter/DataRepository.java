package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    private static List<Partner> partners = new ArrayList<>();
    private static List<Training> trainings = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();

    private static final String FILE_PATH = "fitness_data.dat";

    public static List<Partner> getPartners() { return partners; }
    public static List<Training> getTrainings() { return trainings; }
    public static List<Reservation> getReservations() { return reservations; }

    // ===== PARTNERS =====
    public static void addPartner(Partner p) {
        if (p.getId() == 0) p.setId(getNextPartnerId());
        partners.add(p);
        saveData();
    }

    public static void removePartner(Partner p) {
        partners.remove(p);
        saveData();
    }

    // ===== TRAININGS =====
    public static void addTraining(Training t) {
        if (t.getId() == 0) t.setId(getNextTrainingId());
        trainings.add(t);
        saveData();
    }

    public static void removeTraining(Training t) {
        trainings.remove(t);
        saveData();
    }

    // ===== RESERVATIONS =====
    public static void addReservation(Reservation r) {
        reservations.add(r);
        saveData();
    }

    public static void removeReservation(Reservation r) {
        reservations.remove(r);
        saveData();
    }

    public static Partner findPartnerById(int id) {
        for (Partner p : partners) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static Training findTrainingById(int id) {
        for (Training t : trainings) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    // ===== SAVE / LOAD =====
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new ArrayList<>(partners));
            oos.writeObject(new ArrayList<>(trainings));
            oos.writeObject(new ArrayList<>(reservations));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            partners = (List<Partner>) ois.readObject();
            trainings = (List<Training>) ois.readObject();

            // compatible si el fichero antiguo no tenía reservas
            try {
                reservations = (List<Reservation>) ois.readObject();
            } catch (EOFException eof) {
                reservations = new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== IDS =====
    private static int getNextPartnerId() {
        int max = 0;
        for (Partner p : partners) if (p.getId() > max) max = p.getId();
        return max + 1;
    }

    private static int getNextTrainingId() {
        int max = 0;
        for (Training t : trainings) if (t.getId() > max) max = t.getId();
        return max + 1;
    }
}
