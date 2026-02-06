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
    @FXML private CheckBox cbSocioActivo;
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
            editingPartner.setActive(cbSocioActivo.isSelected());

            DataRepository.saveData();
            lblSocioMensaje.setText("Socio actualizado.");
        } else {
            // ✅ NUEVO
            Partner p = new Partner(
                    tfSocioUsername.getText(),
                    tfSocioEmail.getText(),
                    tfSocioPhone.getText(),
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