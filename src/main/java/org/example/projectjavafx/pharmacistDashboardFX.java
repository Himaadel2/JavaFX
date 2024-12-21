package org.example.projectjavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class pharmacistDashboardFX extends Application {
    private String username;

    public pharmacistDashboardFX(String username) {
        this.username = username;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Dashboard");

        Label pageTitle = new Label("Dashboard");
        pageTitle.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");

        Button addMedicineBtn = createButton("Add Medicine");
        addMedicineBtn.setOnAction(e -> {
            new addMedicineFX().start(new Stage());
        });

        Button profileBtn = createButton("Profile");
        profileBtn.setOnAction(e -> {
            new ProfileFX(username).start(new Stage());
        });

        Button viewMedicineBtn = createButton("View Medicine");
        viewMedicineBtn.setOnAction(e -> {
            new viewmedicineFX().start(new Stage());
        });

        Button exitBtn = createButton("Exit");
        exitBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit the application?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Exit");
            alert.setHeaderText("Exit");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    System.exit(0);
                }
            });
        });

        Button updateMedicineBtn = createButton("Update Medicine");
        updateMedicineBtn.setOnAction(e -> {
            new updateMedicineFX().start(new Stage());
        });

        Button logoutBtn = createButton("Logout");
        logoutBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");
            alert.setHeaderText("Logout");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    stage.close();
                     new loginFX().start(new Stage());
                }
            });
        });



        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(50, 50, 50, 50));
        gridPane.setVgap(50);
        gridPane.setHgap(50);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(addMedicineBtn, 0, 0);
        gridPane.add(updateMedicineBtn, 1, 0);
        gridPane.add(viewMedicineBtn, 0, 1);
        gridPane.add(profileBtn, 1, 1);
        gridPane.add(logoutBtn, 0, 2);
        gridPane.add(exitBtn, 1, 2);

        VBox vbox = new VBox(20, pageTitle, gridPane);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 20));
        button.setPrefSize(250, 100);
        return button;
    }

}