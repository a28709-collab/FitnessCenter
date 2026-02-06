package com.svalero.fitnesscenter;


import com.svalero.fitnesscenter.model.Partner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ApplicationController {
    @FXML private TextField tfSocioUsername, tfSocioEmail, tfSocioPhone;
    @FXML private DatePicker dpSocioFechaAlta;
    @FXML private CheckBox cbClaseDisponible; // Este es el ID de tu CheckBox de socios
    @FXML private ListView<Partner> lvClases;  // Este es el ID de tu ListView de socios
    @FXML private Label lblSocioMensaje;

    private ObservableList<Partner> allPartners = FXCollections.observableArrayList();
    private final String FILE_NAME = "partners.dat";

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        allPartners = FXCollections.observableArrayList();
        loadData(); // Carga los datos al arrancar
        lvClases.setItems(allPartners);

        // Al seleccionar un socio, cargamos sus datos en el formulario
        lvClases.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showPartner(newValue);
        });
    }

    @FXML
    public void savePartner() {
        String username = tfSocioUsername.getText();
        String email = tfSocioEmail.getText();
        String phone = tfSocioPhone.getText();
        LocalDate date = dpSocioFechaAlta.getValue();
        boolean active = cbClaseDisponible.isSelected();

        // VALIDACIÓN (Requisito: 2 campos obligatorios y 1 restricción de formato)
        if (username.isEmpty() || email.isEmpty()) {
            lblSocioMensaje.setText("Error: Username y Email son obligatorios.");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            lblSocioMensaje.setText("Error: El formato del Email no es válido.");
            return;
        }

        Partner newPartner = new Partner(username, email, phone, date, active);
        allPartners.add(newPartner);
        saveData(); // Guardado automático (Persistencia transparente)
        clearPartnerFields();
        lblSocioMensaje.setText("Socio guardado con éxito.");
    }
    @FXML
    public void deletePartner() {
        Partner selected = lvClases.getSelectionModel().getSelectedItem();
        if (selected != null) {
            allPartners.remove(selected);
            saveData();
            lblSocioMensaje.setText("Socio eliminado.");
        } else {
            lblSocioMensaje.setText("Selecciona un socio en la lista para eliminar.");
        }
    }
    @FXML
    public void editPartner() {
        // Carga los datos del socio seleccionado de la lista a los campos de texto
        Partner selected = lvClases.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showPartner(selected);
            lblSocioMensaje.setText("Editando socio: " + selected.getUsername());
        }
    }

    @FXML
    public void newPartner() {
        clearPartnerFields();
        lblSocioMensaje.setText("Formulario de socio limpiado.");
    }

    // --- PERSISTENCIA TRANSPARENTE ---

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new java.util.ArrayList<>(allPartners));
        } catch (IOException e) {
            lblSocioMensaje.setText("Error al guardar datos.");
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                java.util.ArrayList<Partner> loadedPartners = (java.util.ArrayList<Partner>) ois.readObject();
                allPartners.addAll(loadedPartners);
            } catch (Exception e) {
                lblSocioMensaje.setText("Error al cargar datos.");
            }
        }
    }

    // Métodos auxiliares
    private void showPartner(Partner partner) {
        tfSocioUsername.setText(partner.getUsername());
        tfSocioEmail.setText(partner.getEmail());
        tfSocioPhone.setText(partner.getPhone());
        dpSocioFechaAlta.setValue(partner.getDate());
        cbClaseDisponible.setSelected(partner.isActive());
    }

    private void clearPartnerFields() {
        tfSocioUsername.clear();
        tfSocioEmail.clear();
        tfSocioPhone.clear();
        dpSocioFechaAlta.setValue(null);
    }
}
