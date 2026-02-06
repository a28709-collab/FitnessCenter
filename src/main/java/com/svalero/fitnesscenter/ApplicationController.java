package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.Partner;
import com.svalero.fitnesscenter.model.Training;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.regex.Pattern;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    @FXML private TextField tfSocioUsername, tfSocioEmail, tfSocioPhone;
    @FXML private DatePicker dpSocioFechaAlta;
    @FXML private CheckBox cbSocioActivo;
    @FXML private ListView<Partner> lvClases;
    @FXML private Label lblSocioMensaje;


    private Partner editingPartner = null;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{9}$"); // 9 dígitos


    private ObservableList<Partner> allPartners;

    @Override

    public void initialize(URL url, ResourceBundle rb) {

        DataRepository.loadData();
        allPartners = FXCollections.observableArrayList(DataRepository.getPartners());
        lvClases.setItems(allPartners);

        lvClases.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                editingPartner = newV;
                showPartner(newV);
                setFormDisabled(true);
                lblSocioMensaje.setText("Socio seleccionado. Pulsa Modify para editar.");
            }
        });
        clearPartnerFields();
        setFormDisabled(true);
        lblSocioMensaje.setText("Listo para crear socios.");

    }

    @FXML
    public void savePartner() {

        if (editingPartner != null && tfSocioUsername.isDisabled()) {
            lblSocioMensaje.setText("Pulsa Modify antes de guardar cambios.");
            return;
        }

        String username = tfSocioUsername.getText().trim();
        String email = tfSocioEmail.getText().trim();
        String phone = tfSocioPhone.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            lblSocioMensaje.setText("Error: Username y Email son obligatorios.");
            return;
        }

        if (username.length() < 3) {
            lblSocioMensaje.setText("Error: Username debe tener al menos 3 caracteres.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            lblSocioMensaje.setText("Error: Email inválido (ej: nombre@correo.com).");
            return;
        }

        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            lblSocioMensaje.setText("Error: Teléfono inválido (debe tener 9 dígitos).");
            return;
        }


        if (editingPartner != null) {
            // EDITAR
            editingPartner.setUsername(username);
            editingPartner.setEmail(email);
            editingPartner.setPhone(phone);
            editingPartner.setDate(dpSocioFechaAlta.getValue());
            editingPartner.setActive(cbSocioActivo.isSelected());

            DataRepository.saveData();
            lblSocioMensaje.setText("Socio actualizado.");
        } else {
            // NUEVO
            Partner p = new Partner(
                    username,
                    email,
                    phone,
                    dpSocioFechaAlta.getValue(),
                    cbSocioActivo.isSelected()
            );

            DataRepository.addPartner(p);
            lblSocioMensaje.setText("Socio creado.");
        }

        allPartners.setAll(DataRepository.getPartners());
        lvClases.refresh();

        clearPartnerFields();
        editingPartner = null;
        lvClases.getSelectionModel().clearSelection();
        setFormDisabled(true);
    }


    @FXML
    public void modifyPartner() {
        Partner selected = lvClases.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblSocioMensaje.setText("Selecciona un socio para modificar.");
            return;
        }

        editingPartner = selected;
        showPartner(selected);
        setFormDisabled(false);
        lblSocioMensaje.setText("Editando socio... (pulsa Save para guardar)");
    }

    @FXML
    public void openTablesView() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Tables.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            Stage stage = new Stage();
            stage.setTitle("Tables View");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deletePartner() {
        Partner selected = lvClases.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblSocioMensaje.setText("Selecciona un socio para eliminar.");
            return;
        }

        DataRepository.removePartner(selected);

        allPartners.setAll(DataRepository.getPartners());
        lvClases.refresh();

        editingPartner = null;
        lvClases.getSelectionModel().clearSelection();
        clearPartnerFields();
        setFormDisabled(true);

        lblSocioMensaje.setText("Socio eliminado.");
    }

    private void showPartner(Partner p) {
        tfSocioUsername.setText(p.getUsername());
        tfSocioEmail.setText(p.getEmail());
        tfSocioPhone.setText(p.getPhone());
        dpSocioFechaAlta.setValue(p.getDate());
        cbSocioActivo.setSelected(p.isActive());

    }

    private void clearPartnerFields() {
        tfSocioUsername.clear(); tfSocioEmail.clear(); tfSocioPhone.clear();
        dpSocioFechaAlta.setValue(null); cbSocioActivo.setSelected(false);

    }

    @FXML
    public void newPartner() {
        editingPartner = null;
        lvClases.getSelectionModel().clearSelection();
        clearPartnerFields();
        setFormDisabled(false);
        lblSocioMensaje.setText("Nuevo socio");
    }

    private void setFormDisabled(boolean disabled) {
        tfSocioUsername.setDisable(disabled);
        tfSocioEmail.setDisable(disabled);
        tfSocioPhone.setDisable(disabled);
        dpSocioFechaAlta.setDisable(disabled);
        cbSocioActivo.setDisable(disabled);
    }


    @FXML public void saveTraining() {}
    @FXML public void newTraining() {}
    @FXML public void editTraining() {}
    @FXML public void modifyTraining() {}
    @FXML public void deleteTraining() {}
}