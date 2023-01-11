package com.example.crosscuttingprojectapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import processors.ProcessingJsonFile;
import processors.ProcessingTxtFile;
import processors.ProcessingXmlFile;

public class Controller {
    private Stage stage;
    @FXML
    private CheckBox checkEncr;

    @FXML
    private CheckBox checkZip;
    @FXML
    private Button calculateButton;

    @FXML
    private ToggleGroup fileFormat;

    @FXML
    private Button inputButton;

    @FXML
    private TextField inputField;

    @FXML
    private Button outputButton;

    @FXML
    private TextField outputField;

    @FXML
    private RadioButton radioButtonJSON;

    @FXML
    private RadioButton radioButtonTXT;

    @FXML
    private RadioButton radioButtonXML;

    @FXML
    private RadioButton radioButtonNo;

    @FXML
    private RadioButton radioButtonNo1;

    @FXML
    private RadioButton radioButtonYes;

    @FXML
    private RadioButton radioButtonYes1;

    @FXML
    private ToggleGroup ynGroup1;

    @FXML
    private ToggleGroup ynGroup;

    public void Process(ActionEvent event) throws Exception {
        String inputFileName = inputField.getText();
        String outputFileName = outputField.getText();
        boolean isZip = checkZip.isSelected();
        boolean isEncrypt = checkEncr.isSelected();
        boolean isNeedZip = radioButtonYes.isSelected();
        boolean isNeedEncrypt = radioButtonYes1.isSelected();
        if (radioButtonJSON.isSelected()) {
            ProcessingJsonFile pjf = new ProcessingJsonFile(inputFileName);
            pjf.writeToFile(outputFileName);
        }
        else if (radioButtonTXT.isSelected()) {
            ProcessingTxtFile ptf = new ProcessingTxtFile(inputFileName);
            ptf.writeToFile(outputFileName);
        }
        else {
            ProcessingXmlFile pxf = new ProcessingXmlFile(inputFileName);
            pxf.writeToFile(outputFileName);
        }
        stage = (Stage) calculateButton.getScene().getWindow();
        stage.close();
    }
}