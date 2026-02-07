package com.svalero.fitnesscenter;
import com.svalero.fitnesscenter.model.Partner;
import com.svalero.fitnesscenter.model.Reservation;
import com.svalero.fitnesscenter.model.Training;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    @FXML private ComboBox<Partner> cbReservaSocio;
    @FXML private ComboBox<Training> cbReservaClase;
    @FXML private TextField tfReservaPrecioFinal;
    @FXML private CheckBox cbReservaPagado;
    @FXML private DatePicker dpReservaFecha;

    @FXML private ListView<Reservation> lvReservas;
    @FXML private Label lblReservaMensaje;

    private Reservation editingReservation = null;

    private ObservableList<Reservation> allReservations;
    private ObservableList<Partner> allPartners;
    private ObservableList<Training> allTrainings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        allPartners = FXCollections.observableArrayList(DataRepository.getPartners());
        allTrainings = FXCollections.observableArrayList(DataRepository.getTrainings());
        allReservations = FXCollections.observableArrayList(DataRepository.getReservations());

        cbReservaSocio.setItems(allPartners);
        cbReservaClase.setItems(allTrainings);
        lvReservas.setItems(allReservations);


        // Se ve bonito en el listview
        lvReservas.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) {
                    setText(null);
                } else {
                    Partner p = DataRepository.findPartnerById(r.getPartnerId());
                    Training t = DataRepository.findTrainingById(r.getTrainingId());
                    String ps = (p != null) ? p.toString() : ("PartnerId=" + r.getPartnerId());
                    String ts = (t != null) ? t.toString() : ("TrainingId=" + r.getTrainingId());
                    setText(ps + " | " + ts + " | " + r.getDate()
                            + " | Paid: " + (r.isPaid() ? "Yes" : "No")
                            + " | Price: " + r.getFinalPrice());
                }
            }
        });
        lvReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                editingReservation = newV;
                showReservation(newV);
                setFormDisabled(true);
                lblReservaMensaje.setText("Selected reservation. Press Modify to edit.");
            }
        });


        if (allPartners.isEmpty() || allTrainings.isEmpty()) {
            lblReservaMensaje.setText("Create partnerships and classes before booking.");
        } else {
            lblReservaMensaje.setText("Ready to book.");
        }
        clearFields();
        setFormDisabled(true);

    }
    public void refreshCombos() {
        DataRepository.loadData();
        allPartners.setAll(DataRepository.getPartners());
        allTrainings.setAll(DataRepository.getTrainings());
    }

    @FXML
    public void newReservation() {
        editingReservation = null;
        lvReservas.getSelectionModel().clearSelection();
        clearFields();

        refreshCombos();

        setFormDisabled(false);
        lblReservaMensaje.setText("New reservation");
    }

    @FXML
    public void saveReservation() {

        if (editingReservation != null && cbReservaSocio.isDisabled()) {
            lblReservaMensaje.setText("Press Modify before saving changes.");
            return;
        }

        Partner partner = cbReservaSocio.getValue();
        Training training = cbReservaClase.getValue();
        LocalDate date = dpReservaFecha.getValue();

        if (partner == null || training == null || date == null) {
            lblReservaMensaje.setText("Error: Select partner, class, and date.");
            return;
        }

        float finalPrice;
        try {
            String txt = tfReservaPrecioFinal.getText().trim();
            finalPrice = txt.isEmpty() ? 0f : Float.parseFloat(txt.replace(",", "."));
        } catch (Exception e) {
            lblReservaMensaje.setText("Error: Final price invalid.");
            return;
        }

        boolean paid = cbReservaPagado.isSelected();

        if (editingReservation != null) {

            Reservation updated = new Reservation(
                    partner.getId(),
                    training.getId(),
                    paid,
                    finalPrice,
                    date
            );
            updated.setId(editingReservation.getId());
            DataRepository.updateReservation(updated);

            lblReservaMensaje.setText("Updated reservation.");

        } else {
            Reservation r = new Reservation(partner.getId(), training.getId(), paid, finalPrice, date);
            DataRepository.addReservation(r);
            lblReservaMensaje.setText("Reserve created.");
        }

        editingReservation = null;
        lvReservas.getSelectionModel().clearSelection();
        clearFields();
        setFormDisabled(true);

        allReservations.setAll(DataRepository.getReservations());
        lvReservas.refresh();
    }

    @FXML
    public void modifyReservation() {
        Reservation selected = lvReservas.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblReservaMensaje.setText("Select a reservation to modify.");
            return;
        }

        editingReservation = selected;
        showReservation(selected);
        setFormDisabled(false);

        lblReservaMensaje.setText("Editing reservation... (press Save to save)");
    }

    @FXML
    public void deleteReservation() {
        Reservation selected = lvReservas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblReservaMensaje.setText("Select a reservation to delete.");
            return;
        }

        DataRepository.removeReservation(selected);
        allReservations.setAll(DataRepository.getReservations());
        lvReservas.refresh();

        editingReservation = null;
        lvReservas.getSelectionModel().clearSelection();
        clearFields();
        setFormDisabled(true);

        lblReservaMensaje.setText("Reservation removed.");
    }

    private void showReservation(Reservation r) {
        cbReservaSocio.setValue(DataRepository.findPartnerById(r.getPartnerId()));
        cbReservaClase.setValue(DataRepository.findTrainingById(r.getTrainingId()));
        tfReservaPrecioFinal.setText(String.valueOf(r.getFinalPrice()));
        cbReservaPagado.setSelected(r.isPaid());
        dpReservaFecha.setValue(r.getDate());
    }

    private void setFormDisabled(boolean disabled) {
        cbReservaSocio.setDisable(disabled);
        cbReservaClase.setDisable(disabled);
        tfReservaPrecioFinal.setDisable(disabled);
        cbReservaPagado.setDisable(disabled);
        dpReservaFecha.setDisable(disabled);
    }


    private void clearFields() {
        cbReservaSocio.setValue(null);
        cbReservaClase.setValue(null);
        tfReservaPrecioFinal.clear();
        cbReservaPagado.setSelected(false);
        dpReservaFecha.setValue(null);
    }
}
