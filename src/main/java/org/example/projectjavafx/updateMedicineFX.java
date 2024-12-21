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

public class updateMedicineFX extends Application {
    private String username;

    @Override
    public void start(Stage stage) {
        Label addressLabel = new Label("UPDATE MEDICINE");
        addressLabel.setAlignment(Pos.TOP_CENTER);
        addressLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label medicineIdLabel = new Label("Medicine ID:");
        TextField medicineIdField = new TextField();
        medicineIdField.setPrefWidth(200);

        Image image2 = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\search.png");
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitHeight(16);
        imageView2.setFitWidth(16);
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(100);
        searchButton.setGraphic(imageView2);
        GridPane.setHalignment(searchButton, HPos.CENTER);
        searchButton.setContentDisplay(ContentDisplay.RIGHT);

        HBox searchBox = new HBox(10, medicineIdLabel, medicineIdField, searchButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPrefWidth(200);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        Label companyLabel = new Label("Company Name:");
        TextField companyField = new TextField();
        companyField.setPrefWidth(200);
        grid.add(companyLabel, 0, 1);
        grid.add(companyField, 1, 1);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        quantityField.setPrefWidth(200);
        grid.add(quantityLabel, 0, 2);
        grid.add(quantityField, 1, 2);

        Label priceLabel = new Label("Price per unit:");
        TextField priceField = new TextField();
        priceField.setPrefWidth(200);
        grid.add(priceLabel, 0, 3);
        grid.add(priceField, 1, 3);

        Label addQuantityLabel = new Label("Add Quantity:");
        TextField addQuantityField = new TextField();
        addQuantityField.setPrefWidth(200);
        grid.add(addQuantityLabel, 0, 4);
        grid.add(addQuantityField, 1, 4);

        Image image = new Image("file:D:\\progamming\\project JavaFX\\src\\main\\resources\\icons\\save.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);

        Button SaveButton = new Button("Save");
        SaveButton.setPrefWidth(200);
        SaveButton.setGraphic(imageView);
        GridPane.setHalignment(SaveButton, HPos.CENTER);
        SaveButton.setContentDisplay(ContentDisplay.RIGHT);
        grid.add(SaveButton, 0, 5, 2, 1);

        SaveButton.setOnAction(event -> {
            try {
                String quantity = quantityField.getText();
                String price = priceField.getText();
                String add = addQuantityField.getText();
                String name = nameField.getText();
                String company = companyField.getText();

                if ((quantity == null || quantity.isEmpty()) &&
                        (price == null || price.isEmpty()) &&
                        (add == null || add.isEmpty()) &&
                        (name == null || name.isEmpty()) &&
                        (company == null || company.isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Update");
                    alert.setHeaderText(null);
                    alert.setContentText("You must modify at least one field to update the medicine.");
                    alert.showAndWait();
                    return;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");

                StringBuilder sqlBuilder = new StringBuilder("UPDATE medicine SET ");
                boolean isUpdated = false;

                if (name != null && !name.isEmpty()) {
                    sqlBuilder.append("name = ?, ");
                    isUpdated = true;
                }
                if (company != null && !company.isEmpty()) {
                    sqlBuilder.append("company = ?, ");
                    isUpdated = true;
                }
                if (add != null && !add.isEmpty() && quantity != null && !quantity.isEmpty()) {
                    sqlBuilder.append("quantity = ?, ");
                    isUpdated = true;
                }
                if (price != null && !price.isEmpty()) {
                    sqlBuilder.append("price_unit = ?, ");
                    isUpdated = true;
                }

                sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
                sqlBuilder.append(" WHERE id_medicine = ?");

                if (!isUpdated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Update");
                    alert.setHeaderText(null);
                    alert.setContentText("You must modify at least one field to update the medicine.");
                    alert.showAndWait();
                    return;
                }

                PreparedStatement pstmt = con.prepareStatement(sqlBuilder.toString());
                int index = 1;
                if (name != null && !name.isEmpty()) pstmt.setString(index++, name);
                if (company != null && !company.isEmpty()) pstmt.setString(index++, company);
                if (add != null && !add.isEmpty() && quantity != null && !quantity.isEmpty()) {
                    int sum = Integer.parseInt(quantity) + Integer.parseInt(add);
                    pstmt.setInt(index++, sum);
                }
                if (price != null && !price.isEmpty()) pstmt.setString(index++, price);

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

        searchButton.setOnAction(event ->{
            try {
                username = medicineIdField.getText();
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM medicine WHERE id_medicine = '" + username + "'");
                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Medicine Check");
                    alert.setHeaderText(null);
                    alert.setContentText("Medicine exists");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Medicine Check");
                    alert.setHeaderText(null);
                    alert.setContentText("Medicine does not exist");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(20, addressLabel, searchBox, grid);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 640, 500);
        stage.setTitle("Update Medicine");
        stage.setScene(scene);
        stage.show();
    }

}