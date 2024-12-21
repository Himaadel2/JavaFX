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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class addUserFX extends Application {
    int CheckUserName;

    @Override
    public void start(Stage stage) {
        Label addressLabel = new Label("ADD USER");
        addressLabel.setAlignment(Pos.TOP_CENTER);
        addressLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

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

        Label Username = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(200);
        grid.add(Username, 2, 2);
        grid.add(usernameField, 3, 2);

        Label Password = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        grid.add(Password, 0, 3);
        grid.add(passwordField, 1, 3);

        Label Address = new Label("Address:");
        TextField addressField = new TextField();
        addressField.setPrefWidth(200);
        grid.add(Address, 2, 3);
        grid.add(addressField, 3, 3);

        Image image = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\save.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);

        Button SaveButton = new Button("Save");
        SaveButton.setPrefWidth(200);
        SaveButton.setGraphic(imageView);
        SaveButton.setContentDisplay(ContentDisplay.RIGHT);

        GridPane.setHalignment(SaveButton, HPos.CENTER);
        grid.add(SaveButton, 0, 6, 4, 1);

        SaveButton.setOnAction(event -> {
            String userrole = choices.getValue();
            String name2 = nameField.getText();
            String dob = null;

            if (dobPicker.getValue() != null) {
                dob = dobPicker.getValue().toString();
            }

            String phoneNumber = phoneField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String address = addressField.getText();

            // Validation
            if (name2.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name is required.");
            } else if (username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Username is required.");
            } else if (password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Password is required.");
            } else {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");

                    String checkQuery = "SELECT COUNT(*) FROM appuser WHERE username = ?";
                    PreparedStatement checkStmt = con.prepareStatement(checkQuery);
                    checkStmt.setString(1, username);
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        showAlert(Alert.AlertType.ERROR, "Duplicate Username", "Username already exists.");
                        con.close();
                        return;
                    }

                    String sql = "INSERT INTO appuser (user_role, name, dob, phone_number, email, username, password, address) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, userrole);
                    pst.setString(2, name2);
                    pst.setString(3, dob);
                    pst.setString(4, phoneNumber);
                    pst.setString(5, email);
                    pst.setString(6, username);
                    pst.setString(7, password);
                    pst.setString(8, address);
                    pst.executeUpdate();

                    con.close();

                    showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");

                    nameField.clear();
                    phoneField.clear();
                    emailField.clear();
                    usernameField.clear();
                    passwordField.clear();
                    addressField.clear();
                    choices.setValue(null);
                    dobPicker.setValue(null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + ex.getMessage());
                }
            }
        });
        VBox vbox = new VBox(30, addressLabel, grid);
        vbox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(addressLabel, new Insets(20, 0, 20, 0));
        VBox.setMargin(SaveButton, new Insets(30, 0, 30, 0));

        Scene scene = new Scene(vbox, 640, 500);
        stage.setTitle("Add User");
        stage.setScene(scene);
        stage.show();
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}