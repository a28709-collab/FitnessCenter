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
        // Cargamos una vez (si ya estaba cargado no pasa nada)
        DataRepository.loadData();

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
                            + " | Pagado: " + (r.isPaid() ? "Sí" : "No")
                            + " | Precio: " + r.getFinalPrice());
                }
            }
        });
        lvReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                editingReservation = newV;
                showReservation(newV);
                lblReservaMensaje.setText("Editando reserva...");
            }
        });

        if (allPartners.isEmpty() || allTrainings.isEmpty()) {
            lblReservaMensaje.setText("⚠️ Crea socios y clases antes de reservar.");
        } else {
            lblReservaMensaje.setText("Listo para reservar.");
        }
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

        lblReservaMensaje.setText("Nueva reserva");
    }

    @FXML
    public void saveReservation() {
        refreshCombos();

        Partner partner = cbReservaSocio.getValue();
        Training training = cbReservaClase.getValue();
        LocalDate date = dpReservaFecha.getValue();

        if (partner == null || training == null || date == null) {
            lblReservaMensaje.setText("Error: selecciona socio, clase y fecha.");
            return;
        }

        float finalPrice;
        try {
            String txt = tfReservaPrecioFinal.getText().trim();
            finalPrice = txt.isEmpty() ? 0f : Float.parseFloat(txt);
        } catch (Exception e) {
            lblReservaMensaje.setText("Error: precio final inválido.");
            return;
        }

        boolean paid = cbReservaPagado.isSelected();

        if (editingReservation != null) {
            // ✅ EDITAR (sin crear otra)
            editingReservation.setPartnerId(partner.getId());
            editingReservation.setTrainingId(training.getId());
            editingReservation.setPaid(paid);
            editingReservation.setFinalPrice(finalPrice);
            editingReservation.setDate(date);

            DataRepository.saveData();
            lblReservaMensaje.setText("Reserva actualizada.");
        } else {
            // ✅ NUEVA
            Reservation r = new Reservation(partner.getId(), training.getId(), paid, finalPrice, date);
            DataRepository.addReservation(r);
            lblReservaMensaje.setText("Reserva creada.");
        }

        allReservations.setAll(DataRepository.getReservations());
        lvReservas.refresh();
        clearFields();
        editingReservation = null;
        lvReservas.getSelectionModel().clearSelection();
    }

    @FXML
    public void deleteReservation() {
        Reservation selected = lvReservas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblReservaMensaje.setText("Selecciona una reserva para eliminar.");
            return;
        }

        DataRepository.removeReservation(selected);
        allReservations.setAll(DataRepository.getReservations());
        lvReservas.refresh();

        editingReservation = null;
        lvReservas.getSelectionModel().clearSelection();
        clearFields();

        lblReservaMensaje.setText("Reserva eliminada.");
    }

    private void showReservation(Reservation r) {
        cbReservaSocio.setValue(DataRepository.findPartnerById(r.getPartnerId()));
        cbReservaClase.setValue(DataRepository.findTrainingById(r.getTrainingId()));
        tfReservaPrecioFinal.setText(String.valueOf(r.getFinalPrice()));
        cbReservaPagado.setSelected(r.isPaid());
        dpReservaFecha.setValue(r.getDate());
    }

    private void clearFields() {
        cbReservaSocio.setValue(null);
        cbReservaClase.setValue(null);
        tfReservaPrecioFinal.clear();
        cbReservaPagado.setSelected(false);
        dpReservaFecha.setValue(null);
    }
}
