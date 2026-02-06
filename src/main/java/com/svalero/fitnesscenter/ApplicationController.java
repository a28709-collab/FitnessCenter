package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.Partner;
import com.svalero.fitnesscenter.model.Training;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    @FXML private TextField tfSocioUsername, tfSocioEmail, tfSocioPhone;
    @FXML private DatePicker dpSocioFechaAlta;
    @FXML private CheckBox cbClaseDisponible;
    @FXML private ListView<Partner> lvClases;
    @FXML private Label lblSocioMensaje;


    private Partner editingPartner = null;

    private ObservableList<Partner> allPartners;

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        DataRepository.loadData();
        allPartners = FXCollections.observableArrayList(DataRepository.getPartners());
        lvClases.setItems(allPartners);

        lvClases.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                editingPartner = newV;   // ✅ ESTE ES EL PUNTO
                showPartner(newV);
            }
        });
    }


    @FXML
    public void savePartner() {

        if (tfSocioUsername.getText().isEmpty() || tfSocioEmail.getText().isEmpty()) {
            lblSocioMensaje.setText("Error: Campos obligatorios vacíos.");
            return;
        }

        if (editingPartner != null) {
            // ✅ EDITAR
            editingPartner.setUsername(tfSocioUsername.getText());
            editingPartner.setEmail(tfSocioEmail.getText());
            editingPartner.setPhone(tfSocioPhone.getText());
            editingPartner.setDate(dpSocioFechaAlta.getValue());
            editingPartner.setActive(cbClaseDisponible.isSelected());

            DataRepository.saveData(); // ✅ guardar cambios

            lblSocioMensaje.setText("Socio actualizado.");
        } else {
            // ✅ NUEVO
            Partner p = new Partner(
                    tfSocioUsername.getText(),
                    tfSocioEmail.getText(),
                    tfSocioPhone.getText(),
                    dpSocioFechaAlta.getValue(),
                    cbClaseDisponible.isSelected()
            );

            DataRepository.addPartner(p);
            lblSocioMensaje.setText("Socio creado.");
        }

        allPartners.setAll(DataRepository.getPartners());
        clearPartnerFields();
        editingPartner = null;
    }


    @FXML
    public void deletePartner() {
        Partner selected = lvClases.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DataRepository.removePartner(selected);
            allPartners.setAll(DataRepository.getPartners());
            lblSocioMensaje.setText("Socio eliminado.");
        }
    }

    private void showPartner(Partner p) {
        tfSocioUsername.setText(p.getUsername());
        tfSocioEmail.setText(p.getEmail());
        tfSocioPhone.setText(p.getPhone());
        dpSocioFechaAlta.setValue(p.getDate());
        cbClaseDisponible.setSelected(p.isActive());
    }

    private void clearPartnerFields() {
        tfSocioUsername.clear(); tfSocioEmail.clear(); tfSocioPhone.clear();
        dpSocioFechaAlta.setValue(null); cbClaseDisponible.setSelected(false);
    }

    // Métodos para evitar errores de FXML
    @FXML
    public void newPartner() {
        editingPartner = null;   // ✅ importante
        clearPartnerFields();
        lblSocioMensaje.setText("Nuevo socio");
    }

    @FXML public void editPartner() {}
    @FXML public void modifyPartner() {}
    @FXML public void saveTraining() {}
    @FXML public void newTraining() {}
    @FXML public void editTraining() {}
    @FXML public void modifyTraining() {}
    @FXML public void deleteTraining() {}
}