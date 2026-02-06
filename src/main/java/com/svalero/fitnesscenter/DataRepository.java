package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static List<Partner> partners = new ArrayList<>();
    private static List<Training> trainings = new ArrayList<>();
    private static final String FILE_PATH = "fitness_data.dat";

    public static List<Partner> getPartners() { return partners; }
    public static void addPartner(Partner p) { partners.add(p); saveData(); }
    public static void removePartner(Partner p) { partners.remove(p); saveData(); }

    public static List<Training> getTrainings() { return trainings; }
    public static void addTraining(Training t) { trainings.add(t); saveData(); }

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new ArrayList<>(partners));
            oos.writeObject(new ArrayList<>(trainings));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            partners = (List<Partner>) ois.readObject();
            trainings = (List<Training>) ois.readObject();
        } catch (Exception e) { e.printStackTrace(); }
    }
}