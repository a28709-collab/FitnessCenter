package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.Training;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TrainingController implements Initializable {

    @FXML private TextField tfClaseNombre, tfClaseEntrenador, tfClasePrecio;
    @FXML private DatePicker dpClaseFecha;
    @FXML private CheckBox cbClaseDisponible;

    @FXML private ListView<Training> lvClasesGym;
    @FXML private Label lblClaseMensaje;
    @FXML private Spinner<Integer> spClaseDuracion;



    private Training editingTraining = null;
    private ObservableList<Training> allTrainings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        spClaseDuracion.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 60)
        );

        DataRepository.loadData();

        allTrainings = FXCollections.observableArrayList(DataRepository.getTrainings());
        lvClasesGym.setItems(allTrainings);


        lvClasesGym.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                editingTraining = newV;
                showTraining(newV);
                lblClaseMensaje.setText("Clase seleccionada. Pulsa Modify para editar.");
                setFormDisabled(true);
            }
        });

        clearFields();
        setFormDisabled(true);
        lblClaseMensaje.setText("Listo para crear clases.");
    }

    @FXML
    public void newTraining() {
        editingTraining = null;
        lvClasesGym.getSelectionModel().clearSelection();
        clearFields();
        setFormDisabled(false);
        lblClaseMensaje.setText("Nueva clase");
    }

    @FXML
    public void saveTraining() {

        if (editingTraining != null && tfClaseNombre.isDisabled()) {
            lblClaseMensaje.setText("Pulsa Modify antes de guardar cambios.");
            return;
        }

        String name = tfClaseNombre.getText().trim();
        String coach = tfClaseEntrenador.getText().trim();
        LocalDate date = dpClaseFecha.getValue();

        if (name.isEmpty() || coach.isEmpty() || date == null) {
            lblClaseMensaje.setText("Error: Name, Coach y Date son obligatorios.");
            return;
        }

        if (name.length() < 3) {
            lblClaseMensaje.setText("Error: Name debe tener al menos 3 caracteres.");
            return;
        }

        if (coach.length() < 3) {
            lblClaseMensaje.setText("Error: Coach debe tener al menos 3 caracteres.");
            return;
        }

        int duration = spClaseDuracion.getValue();
        if (duration <= 0) {
            lblClaseMensaje.setText("Error: Duration debe ser mayor que 0.");
            return;
        }

        float price;
        try {
            price = Float.parseFloat(tfClasePrecio.getText().trim().replace(",", "."));
        } catch (Exception e) {
            lblClaseMensaje.setText("Error: precio inválido (ej: 12.5).");
            return;
        }

        if (price <= 0) {
            lblClaseMensaje.setText("Error: el precio debe ser mayor que 0.");
            return;
        }

        boolean available = cbClaseDisponible.isSelected();

        if (editingTraining != null) {
            editingTraining.setName(name);
            editingTraining.setCoach(coach);
            editingTraining.setDuration(duration);
            editingTraining.setPrice(price);
            editingTraining.setDate(date);
            editingTraining.setAvailable(available);

            DataRepository.saveData();
            lblClaseMensaje.setText("Clase actualizada.");
        } else {
            Training t = new Training(name, coach, duration, price, date, available);
            DataRepository.addTraining(t);
            lblClaseMensaje.setText("Clase creada.");
        }

        allTrainings.setAll(DataRepository.getTrainings());
        lvClasesGym.refresh();

        clearFields();
        editingTraining = null;
        lvClasesGym.getSelectionModel().clearSelection();
        setFormDisabled(true);
    }

    @FXML
    public void modifyTraining() {
        Training selected = lvClasesGym.getSelectionModel().getSelectedItem();

        if (selected == null) {
            lblClaseMensaje.setText("Selecciona una clase para modificar.");
            return;
        }

        editingTraining = selected;
        showTraining(selected);
        setFormDisabled(false);
        lblClaseMensaje.setText("Editando clase... (pulsa Save para guardar)");
    }



    @FXML
    public void deleteTraining() {
        Training selected = lvClasesGym.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblClaseMensaje.setText("Selecciona una clase para eliminar.");
            return;
        }
        DataRepository.removeTraining(selected);
        allTrainings.setAll(DataRepository.getTrainings());
        lvClasesGym.refresh();

        editingTraining = null;
        lvClasesGym.getSelectionModel().clearSelection();
        clearFields();
        setFormDisabled(true);

        lblClaseMensaje.setText("Clase eliminada.");

    }
    private void setFormDisabled(boolean disabled) {
        tfClaseNombre.setDisable(disabled);
        tfClaseEntrenador.setDisable(disabled);
        spClaseDuracion.setDisable(disabled);
        tfClasePrecio.setDisable(disabled);
        dpClaseFecha.setDisable(disabled);
        cbClaseDisponible.setDisable(disabled);
    }


    private void showTraining(Training t) {
        tfClaseNombre.setText(t.getName());
        tfClaseEntrenador.setText(t.getCoach());
        spClaseDuracion.getValueFactory().setValue(t.getDuration());
        tfClasePrecio.setText(String.valueOf(t.getPrice()));
        dpClaseFecha.setValue(t.getDate());
        cbClaseDisponible.setSelected(t.isAvailable());
    }

    private void clearFields() {
        tfClaseNombre.clear();
        tfClaseEntrenador.clear();
        spClaseDuracion.getValueFactory().setValue(60);
        tfClasePrecio.clear();
        dpClaseFecha.setValue(null);
        cbClaseDisponible.setSelected(false);
    }
}
