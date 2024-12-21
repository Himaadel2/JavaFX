package org.example.projectjavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileFX extends Application {
    private String username;

    public ProfileFX(String username) {
        this.username = username;
    }

    @Override
    public void start(Stage stage) {
        Label titleLabel = new Label("Profile");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");


        ImageView profileImageView = new ImageView(new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\profile1.png"));
        profileImageView.setFitWidth(200);
        profileImageView.setFitHeight(200);

        Label profileNameLabel = new Label("Name");
        profileNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setPadding(new Insets(20));

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label phoneNumberLabel = new Label("Phone Number:");
        TextField phoneNumberField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();

        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(phoneNumberLabel, 0, 1);
        formGrid.add(phoneNumberField, 1, 1);
        formGrid.add(emailLabel, 0, 2);
        formGrid.add(emailField, 1, 2);
        formGrid.add(addressLabel, 0, 3);
        formGrid.add(addressField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required!", ButtonType.OK);
                alert.show();
            } else {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
                    Statement st = con.createStatement();
                    String sql = "UPDATE appuser SET name = '" + name + "', phone_number = '" + phoneNumber + "', email = '" + email + "', address = '" + address + "' WHERE username = '" + username + "';";
                    st.executeUpdate(sql);
                    con.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile updated successfully!", ButtonType.OK);
                    alert.show();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                    alert.show();
                }
            }
        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
            Statement st = con.createStatement();
            String sql = "SELECT * FROM appuser WHERE username = '" + username + "'";
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                profileNameLabel.setText(rs.getString("name"));
                nameField.setText(rs.getString("name"));
                phoneNumberField.setText(rs.getString("phone_number"));
                emailField.setText(rs.getString("email"));
                addressField.setText(rs.getString("address"));
            }
            con.close();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }

        VBox profileBox = new VBox(10, profileImageView, profileNameLabel);
        profileBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, titleLabel, new HBox(20, profileBox, formGrid), saveButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 800, 600);
        stage.setTitle("Profile");
        stage.setScene(scene);
        stage.show();
    }
}
