package org.example.projectjavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class addMedicineFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Medicine");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titleLabel = new Label("Add Medicine");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.TOP_CENTER);


        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(20);
        form.setAlignment(Pos.CENTER);

        Label medicineIdLabel = new Label("Medicine ID:");
        TextField medicineIdField = new TextField();
        form.add(medicineIdLabel, 0, 0);
        form.add(medicineIdField, 1, 0);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        form.add(nameLabel, 0, 1);
        form.add(nameField, 1, 1);

        Label companyLabel = new Label("Company Name:");
        TextField companyField = new TextField();
        form.add(companyLabel, 0, 2);
        form.add(companyField, 1, 2);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        form.add(quantityLabel, 0, 3);
        form.add(quantityField, 1, 3);

        Label priceLabel = new Label("Price per unit:");
        TextField priceField = new TextField();
        form.add(priceLabel, 0, 4);
        form.add(priceField, 1, 4);

        Button saveButton = new Button("Save");
        HBox buttonContainer = new HBox(saveButton);
        buttonContainer.setAlignment(Pos.CENTER);

        saveButton.setOnAction(e -> {
            String id = medicineIdField.getText();
            String name = nameField.getText();
            String companyName = companyField.getText();
            String quantityText = quantityField.getText();
            String priceText = priceField.getText();

            try {
                int quantity = Integer.parseInt(quantityText);
                double price = Double.parseDouble(priceText);

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
                String sql = "INSERT INTO medicine (id_medicine, name, company, quantity, price_unit) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, companyName);
                pst.setInt(4, quantity);
                pst.setDouble(5, price);

                pst.executeUpdate();
                con.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Medicine added successfully!");
                alert.showAndWait();

                medicineIdField.clear();
                nameField.clear();
                companyField.clear();
                quantityField.clear();
                priceField.clear();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid input for quantity or price.");
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add medicine.\n" + ex.getMessage());
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(titleLabel, form, buttonContainer);
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
