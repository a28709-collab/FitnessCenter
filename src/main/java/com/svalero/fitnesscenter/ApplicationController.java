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

    private ObservableList<Partner> allPartners;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carga inicial desde el repositorio
        DataRepository.loadData();
        allPartners = FXCollections.observableArrayList(DataRepository.getPartners());
        lvClases.setItems(allPartners);

        // Listener para navegar
        lvClases.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) showPartner(newV);
        });
    }

    @FXML
    public void savePartner() {
        if (tfSocioUsername.getText().isEmpty() || tfSocioEmail.getText().isEmpty()) {
            lblSocioMensaje.setText("Error: Campos obligatorios vacíos.");
            return;
        }

        Partner p = new Partner(tfSocioUsername.getText(), tfSocioEmail.getText(),
                tfSocioPhone.getText(), dpSocioFechaAlta.getValue(),
                cbClaseDisponible.isSelected());

        // Guardar en Repositorio y actualizar lista visual
        DataRepository.addPartner(p);
        allPartners.setAll(DataRepository.getPartners());

        clearPartnerFields();
        lblSocioMensaje.setText("Socio guardado en el archivo.");
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
    @FXML public void newPartner() { clearPartnerFields(); }
    @FXML public void editPartner() {}
    @FXML public void modifyPartner() {}
    @FXML public void saveTraining() {}
    @FXML public void newTraining() {}
    @FXML public void editTraining() {}
    @FXML public void modifyTraining() {}
    @FXML public void deleteTraining() {}
}