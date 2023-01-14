package com.example.crosscuttingprojectapplication;

import archiving.ArchivingGen;
import encrypting.EncryptGen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import processors.ProcessingFile;
import processors.ProcessingJsonFile;
import processors.ProcessingTxtFile;
import processors.ProcessingXmlFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

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
    private FileChannel desktop;

    public void selectInputFile(ActionEvent actionEvent) {
        Stage primaryStage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\sivog\\IdeaProjects\\CrossCuttingProjectApplication\\__fixtures__"));
        inputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inputField.clear();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile(file);
                    List<File> files = Arrays.asList(file);
                    printLog(inputField, files);
                }
            }
        });

        outputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                outputField.clear();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile(file);
                    List<File> files = Arrays.asList(file);
                    printLog(outputField, files);
                }
            }
        });
    }

    private void printLog(TextField textArea, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textArea.appendText(file.getAbsolutePath() + "\n");
        }
    }

    private void openFile(File file) {
        try {
            this.desktop.open(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Process(ActionEvent event) throws Exception {
        String inputFileName = inputField.getText();
        String outputFileName = outputField.getText();
        boolean isZip = checkZip.isSelected();
        boolean isEncrypt = checkEncr.isSelected();
        boolean isNeedZip = radioButtonYes.isSelected();
        boolean isNeedEncrypt = radioButtonYes1.isSelected();
        if (radioButtonJSON.isSelected()) {
            processInputFile("json", isZip, isEncrypt, isNeedEncrypt, isNeedZip);
        }
        else if (radioButtonTXT.isSelected()) {
            processInputFile("txt", isZip, isEncrypt, isNeedEncrypt, isNeedZip);
        }
        else {
            processInputFile("xml", isZip, isEncrypt, isNeedEncrypt, isNeedZip);
        }

        stage = (Stage) calculateButton.getScene().getWindow();
        stage.close();
    }

    public void processInputFile(String typeFile, boolean isZip, boolean isEncrypt,
                        boolean isNeedEncrypt, boolean isNeedZip) throws Exception {
        String inputFileName = inputField.getText();
        String outputFileName = outputField.getText();
        boolean isNeedProcessOutputFile = (isNeedEncrypt || isNeedZip);
        ProcessingFile processingFile = null;
        if (isZip && isEncrypt) {
            File unzipFile = getUnzipFile(inputFileName, typeFile);
            String unzipFileName = "__fixtures__/" + unzipFile.getName();
            File decryptFile = getDecryptFile(unzipFileName, typeFile);
            if (typeFile == "txt") {
                processingFile = new ProcessingTxtFile("__fixtures__/" + decryptFile.getName());
            }
            else if (typeFile == "xml") {
                processingFile = new ProcessingXmlFile("__fixtures__/" + decryptFile.getName());
            }
            else if (typeFile == "json") {
                processingFile = new ProcessingJsonFile("__fixtures__/" + decryptFile.getName());
            }

            if (isNeedProcessOutputFile) {
                processOutputFile(processingFile, typeFile);
            }
            else {
                processingFile.writeResultToFile(outputFileName);
            }
            decryptFile.delete();
            unzipFile.delete();
        }
        else if (isZip) {
            File file = getUnzipFile(inputFileName, typeFile);
            if (typeFile == "txt") {
                processingFile = new ProcessingTxtFile("__fixtures__/" + file.getName());
            }
            else if (typeFile == "xml") {
                processingFile = new ProcessingXmlFile("__fixtures__/" + file.getName());
            }
            else if (typeFile == "json") {
                processingFile = new ProcessingJsonFile("__fixtures__/" + file.getName());
            }

            if (isNeedProcessOutputFile) {
                processOutputFile(processingFile, typeFile);
            }
            else {
                processingFile.writeResultToFile(outputFileName);
            }
            file.delete();
        }
        else if (isEncrypt) {
            File file = getDecryptFile(inputFileName, typeFile);
            if (typeFile == "txt") {
                processingFile = new ProcessingTxtFile("__fixtures__/" + file.getName());
            }
            else if (typeFile == "xml") {
                processingFile = new ProcessingXmlFile("__fixtures__/" + file.getName());
            }
            else if (typeFile == "json") {
                processingFile = new ProcessingJsonFile("__fixtures__/" + file.getName());
            }

            if (isNeedProcessOutputFile) {
                processOutputFile(processingFile, typeFile);
            }
            else {
                processingFile.writeResultToFile(outputFileName);
            }
            file.delete();
        }
        else {
            if (typeFile == "txt") {
                processingFile = new ProcessingTxtFile(inputFileName);
            }
            else if (typeFile == "xml") {
                processingFile = new ProcessingXmlFile(inputFileName);
            }
            else if (typeFile == "json") {
                processingFile = new ProcessingJsonFile(inputFileName);
            }

            if (isNeedProcessOutputFile) {
                processOutputFile(processingFile, typeFile);
            }
            else {
                processingFile.writeResultToFile(outputFileName);
            }
        }
    }

    public void processOutputFile(ProcessingFile processingFile, String typeFile) throws Exception {
        String outputFileName = outputField.getText();
        boolean isNeedZip = radioButtonYes.isSelected();
        boolean isNeedEncrypt = radioButtonYes1.isSelected();
        if (isNeedZip && isNeedEncrypt) {
            String firstTempFileName = "__fixtures__/firstTemp." + typeFile;
            String secondTempFileName = "__fixtures__/secondTemp." + typeFile;
            File secondTempFile = new File(secondTempFileName);
            EncryptGen encryptGen = new EncryptGen();
            ArchivingGen archivingGen = new ArchivingGen();
            processingFile.writeResultToFile(firstTempFileName);
            encryptGen.encryptFile(firstTempFileName, secondTempFileName);
            File newF = new File(firstTempFileName);
            newF.delete();
            archivingGen.zipFile(secondTempFileName, outputFileName);
            secondTempFile.delete();
        }
        else if (isNeedZip) {
            String tempFileName = "__fixtures__/temp." + typeFile;
            File file = new File(tempFileName);
            ArchivingGen archivingGen = new ArchivingGen();
            processingFile.writeResultToFile(tempFileName);
            archivingGen.zipFile(tempFileName, outputFileName);
            file.delete();
        }
        else {
            String tempFileName = "__fixtures__/tempdsa." + typeFile;
            File tempFile = new File(tempFileName);
            EncryptGen encryptGen = new EncryptGen();
            processingFile.writeResultToFile(tempFileName);
            encryptGen.encryptFile(tempFileName, outputFileName);
            tempFile.delete();
        }
    }

    public File getDecryptFile(String inputFileName, String typeOfFile) throws Exception {
        String tempFileName = "__fixtures__/tempDecrypt." + typeOfFile;
        File file = new File(tempFileName);
        EncryptGen eg = new EncryptGen();
        eg.decryptFile(inputFileName, tempFileName);
        return file;
    }

    public File getUnzipFile(String inputFileName, String typeOfFile) throws IOException {
        String tempFileName = "__fixtures__/tempZip." + typeOfFile;
        File file = new File(tempFileName);
        ArchivingGen ag = new ArchivingGen();
        ag.unzipFile(inputFileName, tempFileName);
        return file;
    }
}