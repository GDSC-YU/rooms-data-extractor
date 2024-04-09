package com.rooms.gui;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.rooms.extractor.DataExtractor;

public class FileChooserGUI extends Application {

    private ProgressBar progressBar;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white");
        Button chooseFileButton = new Button("Choose Excel File");
        chooseFileButton.setStyle("-fx-base: orange; -fx-text-fill: white; -fx-background-radius: 15px;");
        chooseFileButton.setOnAction(event ->chooseFile(primaryStage));
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setStyle("-fx-accent:lightgreen;");
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #FF0000;");

        root.getChildren().addAll(chooseFileButton, progressBar, statusLabel);

        Scene scene = new Scene(root, 400, 300);
        Image programIcon = new Image("gdsc-logo.png");
        primaryStage.getIcons().add(programIcon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("YU Rooms");
        primaryStage.show();
    }

    private void chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            progressBar.setVisible(true); // Show the progress bar
            setStatus("Processing file...", Color.BLACK); // Change status to indicate processing

//            Saving file functionality (removed, no need to save the Excel file)
//            String destinationFolder = "src/main/resources/";
//
//            // Define the destination file path
//            String destinationFilePath = destinationFolder + selectedFile.getName();
//
//            // Create the destination file
//            File destinationFile = new File(destinationFilePath);

//            try {
//                // Copy the selected file to the destination folder
//                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//            } catch (IOException e) {
//                e.printStackTrace();
//                setStatus("Error saving file.", Color.RED);
//            }

            Task<Void> task = new Task<>() {
                @Override
                protected Void call(){
                    DataExtractor.extractData(selectedFile.getPath());
                    return null;
                }
            };
            task.setOnSucceeded(event -> {
                setStatus("File processed successfully.", Color.GREEN);
                progressBar.setVisible(false); // Hide the progress bar

            });

            task.setOnFailed(event -> {
                setStatus("Error processing file. Incorrect Format", Color.RED);
                progressBar.setVisible(false); // Hide the progress bar
            });

            new Thread(task).start();
        } else {
            setStatus("File selection canceled.", Color.RED);
        }
    }

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
