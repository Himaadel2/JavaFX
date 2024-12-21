package org.example.projectjavafx;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class viewmedicineFX extends Application {
    @Override
    public void start(Stage stage) {
        Label addressLabel = new Label("View User");
        addressLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        String[] columns = {"ID", "Medicine ID", "Name", "Company Name", "Quantity", "Price per unit"};

        TableView<Map<String, String>> table = new TableView<>();
        ObservableList<Map<String, String>> medicineList = FXCollections.observableArrayList();

        for (String columnName : columns) {
            TableColumn<Map<String, String>, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(cellData -> {
                if (cellData.getValue().containsKey(columnName)) {
                    return new SimpleStringProperty(cellData.getValue().get(columnName));
                } else {
                    return new SimpleStringProperty("N/A");
                }
            });
            table.getColumns().add(column);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM medicine");

            while (rs.next()) {
                Map<String, String> medicine = new HashMap<>();
                medicine.put("ID", rs.getString("id_m"));
                medicine.put("Medicine ID", rs.getString("id_medicine"));
                medicine.put("Name", rs.getString("name"));
                medicine.put("Company Name", rs.getString("company"));
                medicine.put("Quantity", rs.getString("quantity"));
                medicine.put("Price per unit", rs.getString("price_unit"));

                medicineList.add(medicine);
            }
            table.setItems(medicineList);
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setRowFactory(tv -> {
            TableRow<Map<String, String>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Map<String, String> selectedMedicine = row.getItem();
                    String medicine_ID = selectedMedicine.get("ID");

                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
                        PreparedStatement pstmt = con.prepareStatement("DELETE FROM medicine WHERE id_m = ?");
                        pstmt.setString(1, medicine_ID);
                        pstmt.executeUpdate();
                        pstmt.close();
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    medicineList.remove(selectedMedicine);
                }
            });
            return row;
        });

        VBox vbox = new VBox(10, addressLabel, table);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 800, 600);
        stage.setTitle("View Users");
        stage.setScene(scene);
        stage.show();
    }

}
