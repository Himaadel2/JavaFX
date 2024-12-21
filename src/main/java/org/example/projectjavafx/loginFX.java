package org.example.projectjavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class loginFX extends Application {
    String user ;
    @Override
    public void start(Stage primaryStage) {
        Image logoImage = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\logo_login.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitHeight(100);
        logoImageView.setFitWidth(100);

        Label addressLabel = new Label("LOGIN");
        addressLabel.setAlignment(Pos.CENTER);
        addressLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(15));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);

        Label label1 = new Label("User name:");
        TextField textField = new TextField();
        pane.addRow(0, label1, textField);

        Label label2 = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        pane.addRow(1, label2, passwordField);

        Image image = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\login.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);

        Button login = new Button("Login", imageView);
        login.setPadding(new Insets(10));
        login.setContentDisplay(ContentDisplay.RIGHT);
        login.setPrefWidth(120);
        pane.add(login, 1, 2);

        Button reset = new Button("Reset");
        reset.setPadding(new Insets(10));
        reset.setPrefWidth(100);
        pane.add(reset, 2, 2);

        login.setOnAction(event -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");

                String username = textField.getText();
                String password = passwordField.getText();

                String sql = "SELECT * FROM pharmacy.appuser WHERE username = ? AND password = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    ((Stage) login.getScene().getWindow()).close();

                    user = username;
                    if (rs.getString("user_role").equals("Admin")) {
                        new adminDashboardFX(username).start(new Stage());
                    } else {
                        new pharmacistDashboardFX(username).start(new Stage());
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to login");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong username or password");
                    alert.showAndWait();

                    textField.clear();
                    passwordField.clear();
                }
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        reset.setOnAction(event -> {
            textField.clear();
            passwordField.clear();
        });


        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login.fire();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login.fire();
            }
        });

        VBox root = new VBox(20, logoImageView, addressLabel, pane);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 450, 400);

        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
