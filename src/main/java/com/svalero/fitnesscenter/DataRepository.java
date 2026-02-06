package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.Partner;
import com.svalero.fitnesscenter.model.Training;
import com.svalero.fitnesscenter.model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    private static List<Partner> partners = new ArrayList<>();
    private static List<Training> trainings = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();

    // Archivo donde se guardará todo (Persistencia transparente)
    private static final String FILE_PATH = "fitness_data.dat";

    // --- GESTIÓN DE PARTNERS (Socios) ---
    public static List<Partner> getPartners() { return partners; }
    public static void addPartner(Partner p) {
        partners.add(p);
        saveData();
    }
    public static void removePartner(Partner p) {
        partners.remove(p);
        saveData();
    }

    // --- GESTIÓN DE TRAININGS (Clases) ---
    public static List<Training> getTrainings() { return trainings; }
    public static void addTraining(Training t) {
        trainings.add(t);
        saveData();
    }
    public static void removeTraining(Training t) {
        trainings.remove(t);
        saveData();
    }

    // --- GESTIÓN DE RESERVATIONS (Reservas) ---
    public static List<Reservation> getReservations() { return reservations; }
    public static void addReservation(Reservation r) {
        reservations.add(r);
        saveData();
    }
    public static void removeReservation(Reservation r) {
        reservations.remove(r);
        saveData();
    }

    // --- PERSISTENCIA: GUARDADO ---
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new ArrayList<>(partners));
            oos.writeObject(new ArrayList<>(trainings));
            oos.writeObject(new ArrayList<>(reservations));
            System.out.println("Datos guardados correctamente en " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // --- PERSISTENCIA: CARGA ---
    @SuppressWarnings("unchecked")
    public static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            partners = (List<Partner>) ois.readObject();
            trainings = (List<Training>) ois.readObject();
            reservations = (List<Reservation>) ois.readObject();
            System.out.println("Datos cargados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar: " + e.getMessage());
        }
    }
}
