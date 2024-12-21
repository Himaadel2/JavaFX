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

public class viewUserFX extends Application {
    @Override
    public void start(Stage stage) {
        Label addressLabel = new Label("View User");
        addressLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        String[] columns = {"ID", "Name", "Role", "Date of birth", "Mobile phone", "Email", "Username", "Password", "Address"};

        TableView<Map<String, String>> table = new TableView<>();
        ObservableList<Map<String, String>> userList = FXCollections.observableArrayList();

        // إنشاء الأعمدة
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

        // جلب البيانات من قاعدة البيانات
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM appuser");

            while (rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("ID", rs.getString("user_id"));
                user.put("Name", rs.getString("name"));
                user.put("Role", rs.getString("user_role"));
                user.put("Date of birth", rs.getString("dob"));
                user.put("Mobile phone", rs.getString("phone_number"));
                user.put("Email", rs.getString("email"));
                user.put("Username", rs.getString("username"));
                user.put("Password", rs.getString("password"));
                user.put("Address", rs.getString("address"));

                userList.add(user);
            }
            table.setItems(userList);
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // إضافة مستمع للنقر المزدوج
        table.setRowFactory(tv -> {
            TableRow<Map<String, String>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Map<String, String> selectedUser = row.getItem();
                    String userId = selectedUser.get("ID");

                    // حذف السجل من قاعدة البيانات
                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy?useSSL=false", "root", "1234");
                        PreparedStatement pstmt = con.prepareStatement("DELETE FROM appuser WHERE user_id = ?");
                        pstmt.setString(1, userId);
                        pstmt.executeUpdate();
                        pstmt.close();
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // حذف السجل من الجدول
                    userList.remove(selectedUser);
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
