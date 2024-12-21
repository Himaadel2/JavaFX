package org.example.projectjavafx;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class updateUserFX extends Application {
    private String username;

    @Override
    public void start(Stage stage) {
        Label addressLabel = new Label("UPDATE");
        addressLabel.setAlignment(Pos.TOP_CENTER);
        addressLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");


        Label Username = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(200);

        Image image2 = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\search.png");
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitHeight(16);
        imageView2.setFitWidth(16);
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(100);
        searchButton.setGraphic(imageView2);
        GridPane.setHalignment(searchButton, HPos.CENTER);
        searchButton.setContentDisplay(ContentDisplay.RIGHT);

        HBox usernameBox = new HBox(10, Username, usernameField, searchButton);
        usernameBox.setAlignment(Pos.CENTER);
        usernameBox.setPadding(new Insets(5));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        Label userRole = new Label("User Role:");
        ComboBox<String> choices = new ComboBox<>();
        choices.getItems().addAll("Admin", "Pharmacist");
        choices.setPrefWidth(200);
        choices.setValue("Admin");
        grid.add(userRole, 0, 0);
        grid.add(choices, 1, 0);

        Label name = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPrefWidth(200);
        grid.add(name, 2, 0);
        grid.add(nameField, 3, 0);

        Label Date = new Label("Date of Birth:");
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPrefWidth(200);
        grid.add(Date, 0, 1);
        grid.add(dobPicker, 1, 1);

        Label PhoneNumber = new Label("Phone Number:");
        TextField phoneField = new TextField();
        phoneField.setPrefWidth(200);
        grid.add(PhoneNumber, 2, 1);
        grid.add(phoneField, 3, 1);

        Label Email = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPrefWidth(200);
        grid.add(Email, 0, 2);
        grid.add(emailField, 1, 2);

        Label Address = new Label("Address:");
        TextField addressField = new TextField();
        addressField.setPrefWidth(200);
        grid.add(Address, 2, 2);
        grid.add(addressField, 3, 2);

        Image image = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\save.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);

        Button SaveButton = new Button("Save");
        SaveButton.setPrefWidth(200);
        SaveButton.setGraphic(imageView);
        GridPane.setHalignment(SaveButton, HPos.CENTER);
        SaveButton.setContentDisplay(ContentDisplay.RIGHT);
        grid.add(SaveButton, 0, 4, 4, 1);

        SaveButton.setOnAction(event -> {
            try {
                String phoneNumber = phoneField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                String userrole = choices.getValue();
                String name2 = nameField.getText();
                String date = dobPicker.getValue() != null ? dobPicker.getValue().toString() : null;

                boolean isUpdated = false;

                if (name2.isEmpty() && userrole.isEmpty() && phoneNumber.isEmpty() && (date == null || date.isEmpty()) && email.isEmpty() && address.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Update");
                    alert.setHeaderText(null);
                    alert.setContentText("You must modify at least one field to update the user.");
                    alert.showAndWait();
                    return;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");

                StringBuilder sqlBuilder = new StringBuilder("UPDATE appuser SET ");

                if (!name2.isEmpty()) {
                    sqlBuilder.append("name = ?, ");
                    isUpdated = true;
                }
                if (!userrole.isEmpty()) {
                    sqlBuilder.append("user_role = ?, ");
                    isUpdated = true;
                }
                if (!phoneNumber.isEmpty()) {
                    sqlBuilder.append("phone_number = ?, ");
                    isUpdated = true;
                }
                if (date != null && !date.isEmpty()) {
                    sqlBuilder.append("dob = ?, ");
                    isUpdated = true;
                }
                if (!email.isEmpty()) {
                    sqlBuilder.append("email = ?, ");
                    isUpdated = true;
                }
                if (!address.isEmpty()) {
                    sqlBuilder.append("address = ?, ");
                    isUpdated = true;
                }

                if (!isUpdated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Update");
                    alert.setHeaderText(null);
                    alert.setContentText("You must modify at least one field to update the user.");
                    alert.showAndWait();
                    return;
                }

                sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
                sqlBuilder.append(" WHERE username = ?");

                PreparedStatement pstmt = con.prepareStatement(sqlBuilder.toString());
                int index = 1;
                if (!name2.isEmpty()) pstmt.setString(index++, name2);
                if (!userrole.isEmpty()) pstmt.setString(index++, userrole);
                if (!phoneNumber.isEmpty()) pstmt.setString(index++, phoneNumber);
                if (date != null && !date.isEmpty()) pstmt.setString(index++, date);
                if (!email.isEmpty()) pstmt.setString(index++, email);
                if (!address.isEmpty()) pstmt.setString(index++, address);
                pstmt.setString(index, username);

                pstmt.executeUpdate();
                con.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update");
                alert.setHeaderText(null);
                alert.setContentText("Updated successfully!");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update");
                alert.setHeaderText(null);
                alert.setContentText("Error: " + ex.getMessage());
                alert.showAndWait();
            }
        });



        searchButton.setOnAction(event -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM appuser WHERE username = ?");
                username = usernameField.getText();
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Username Check");
                    alert.setHeaderText(null);
                    alert.setContentText("Username exists");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Username Check");
                    alert.setHeaderText(null);
                    alert.setContentText("Username does not exist");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(30, addressLabel, usernameBox, grid);
        vbox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(addressLabel, new Insets(10, 0, 20, 0));
        VBox.setMargin(SaveButton, new Insets(30, 0, 30, 0));

        Scene scene = new Scene(vbox, 640, 500);
        stage.setTitle("Update User");
        stage.setScene(scene);
        stage.show();
    }

}
