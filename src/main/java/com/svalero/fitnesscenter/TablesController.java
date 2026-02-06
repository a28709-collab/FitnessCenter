package com.svalero.fitnesscenter;

import com.svalero.fitnesscenter.model.Partner;
import com.svalero.fitnesscenter.model.Training;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TablesController implements Initializable {

    // ====== TABLE PARTNERS ======
    @FXML private TableView<Partner> tvPartners;
    @FXML private TableColumn<Partner, Integer> colPartnerId;
    @FXML private TableColumn<Partner, String> colPartnerUsername;
    @FXML private TableColumn<Partner, String> colPartnerEmail;
    @FXML private TableColumn<Partner, String> colPartnerPhone;
    @FXML private TableColumn<Partner, Object> colPartnerDate; // LocalDate -> Object para no pelearse con genéricos
    @FXML private TableColumn<Partner, Boolean> colPartnerActive;

    // ====== TABLE TRAININGS ======
    @FXML private TableView<Training> tvTrainings;
    @FXML private TableColumn<Training, Integer> colTrainingId;
    @FXML private TableColumn<Training, String> colTrainingName;
    @FXML private TableColumn<Training, String> colTrainingCoach;
    @FXML private TableColumn<Training, Integer> colTrainingDuration;
    @FXML private TableColumn<Training, Float> colTrainingPrice;
    @FXML private TableColumn<Training, Object> colTrainingDate; // LocalDate
    @FXML private TableColumn<Training, Boolean> colTrainingAvailable;

    private ObservableList<Partner> partners;
    private ObservableList<Training> trainings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Config columnas Partners
        colPartnerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartnerUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPartnerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPartnerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPartnerDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPartnerActive.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Config columnas Trainings
        colTrainingId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTrainingName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTrainingCoach.setCellValueFactory(new PropertyValueFactory<>("coach"));
        colTrainingDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colTrainingPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTrainingDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTrainingAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Carga inicial
        refreshTables();
    }

    @FXML
    public void refreshTables() {
        DataRepository.loadData();

        partners = FXCollections.observableArrayList(DataRepository.getPartners());
        trainings = FXCollections.observableArrayList(DataRepository.getTrainings());

        tvPartners.setItems(partners);
        tvTrainings.setItems(trainings);
        tvPartners.refresh();
        tvTrainings.refresh();
    }
}
