package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.Partner;
import com.svalero.fitnesscenter.model.Training;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class JsonExporter {

    // PARTNERS

    public static void exportPartners(List<Partner> partners, String filename) throws Exception {

        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < partners.size(); i++) {
            Partner p = partners.get(i);

            json.append("  {\n");
            json.append("    \"id\": ").append(p.getId()).append(",\n");
            json.append("    \"username\": \"").append(escape(p.getUsername())).append("\",\n");
            json.append("    \"email\": \"").append(escape(p.getEmail())).append("\",\n");
            json.append("    \"phone\": \"").append(escape(p.getPhone())).append("\",\n");
            json.append("    \"date\": \"").append(formatDate(p.getDate())).append("\",\n");
            json.append("    \"active\": ").append(p.isActive()).append("\n");
            json.append("  }");

            if (i < partners.size() - 1) json.append(",");
            json.append("\n");
        }

        json.append("]\n");

        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(json.toString());
        }
    }

    // TRAININGS

    public static void exportTrainings(List<Training> trainings, String filename) throws Exception {

        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < trainings.size(); i++) {
            Training t = trainings.get(i);

            json.append("  {\n");
            json.append("    \"id\": ").append(t.getId()).append(",\n");
            json.append("    \"name\": \"").append(escape(t.getName())).append("\",\n");
            json.append("    \"coach\": \"").append(escape(t.getCoach())).append("\",\n");
            json.append("    \"duration\": ").append(t.getDuration()).append(",\n");
            json.append("    \"price\": ").append(t.getPrice()).append(",\n");
            json.append("    \"date\": \"").append(formatDate(t.getDate())).append("\",\n");
            json.append("    \"available\": ").append(t.isAvailable()).append("\n");
            json.append("  }");

            if (i < trainings.size() - 1) json.append(",");
            json.append("\n");
        }

        json.append("]\n");

        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(json.toString());
        }
    }

    // UTILS

    private static String escape(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String formatDate(LocalDate date) {
        return (date == null) ? "" : date.toString();
    }
}
