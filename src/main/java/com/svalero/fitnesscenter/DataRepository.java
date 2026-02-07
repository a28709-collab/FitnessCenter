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
        if (r.getId() == 0) r.setId(getNextReservationId());
        reservations.add(r);
        saveData();
    }

    public static void updateReservation(Reservation updated) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == updated.getId()) {
                reservations.set(i, updated);
                saveData();
                return;
            }
        }
    }

    private static int getNextReservationId() {
        int max = 0;
        for (Reservation r : reservations) if (r.getId() > max) max = r.getId();
        return max + 1;
    }

    public static void removeReservation(Reservation r) {
        reservations.removeIf(x -> x.getId() == r.getId());
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

            try {
                reservations = (List<Reservation>) ois.readObject();
            } catch (EOFException eof) {
                reservations = new ArrayList<>();
            }

            // ✅ Asignar IDs a reservas antiguas (id=0)
            int maxId = 0;
            for (Reservation r : reservations) {
                if (r.getId() > maxId) maxId = r.getId();
            }
            for (Reservation r : reservations) {
                if (r.getId() == 0) r.setId(++maxId);
            }

            saveData(); // deja el fichero ya “arreglado”

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void resetDataFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("BORRADO: " + file.getAbsolutePath() + " -> " + deleted);
        } else {
            System.out.println("NO EXISTE: " + file.getAbsolutePath());
        }

        partners = new ArrayList<>();
        trainings = new ArrayList<>();
        reservations = new ArrayList<>();
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
