package org.example.projectjavafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class sellMedicineFX extends Application {

    private TextField medicineIdField;
    private Label totalAmountLabel;
    private TableView<Medicine> medicineTable;
    private TableView<Medicine> cartTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sell Medicine");

        Label pageTitle = new Label("Sell Medicine");
        pageTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        Separator separator = new Separator();

        Label searchLabel = new Label("Search Medicine:");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter medicine name or ID");
        searchField.setOnKeyReleased(e -> fetchMedicineData(searchField.getText()));

        VBox searchSection = new VBox(10, searchLabel, searchField);
        searchSection.setPadding(new Insets(10));

        medicineTable = new TableView<>();
        setUpMedicineTable(medicineTable);

        VBox leftSection = new VBox(10, searchSection, medicineTable);
        leftSection.setPadding(new Insets(10));

        GridPane formGrid = createFormGrid();

        cartTable = new TableView<>();
        setUpMedicineTable(cartTable);

        VBox rightSection = new VBox(10, formGrid, cartTable);
        rightSection.setPadding(new Insets(10));

        Label totalAmountTextLabel = new Label("Total Amount:");
        totalAmountLabel = new Label("0.00");
        HBox bottomSection = new HBox(10, totalAmountTextLabel, totalAmountLabel);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(10));

        HBox mainLayout = new HBox(10, leftSection, rightSection);
        BorderPane root = new BorderPane();
        root.setTop(new VBox(10, pageTitle, separator));
        root.setCenter(mainLayout);
        root.setBottom(bottomSection);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createFormGrid() {
        Label medicineIdLabel = new Label("Medicine ID:");
        medicineIdField = new TextField();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setDisable(true);

        Label companyNameLabel = new Label("Company Name:");
        TextField companyNameField = new TextField();
        companyNameField.setDisable(true);

        Label pricePerUnitLabel = new Label("Price Per Unit:");
        TextField pricePerUnitField = new TextField();
        pricePerUnitField.setDisable(true);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();

        Label totalPriceLabel = new Label("Total Price:");
        TextField totalPriceField = new TextField();
        totalPriceField.setDisable(true);

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            String id = medicineIdField.getText();
            String name = nameField.getText();
            String company = companyNameField.getText();
            String price = pricePerUnitField.getText();
            String quantity = quantityField.getText();

            if (!id.isEmpty() && !name.isEmpty() && !quantity.isEmpty()) {
                cartTable.getItems().add(new Medicine(id, name, company, quantity, price));
                clearFormFields();
            }
        });

        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(medicineIdLabel, 0, 0);
        formGrid.add(medicineIdField, 1, 0);
        formGrid.add(nameLabel, 0, 1);
        formGrid.add(nameField, 1, 1);
        formGrid.add(companyNameLabel, 0, 2);
        formGrid.add(companyNameField, 1, 2);
        formGrid.add(pricePerUnitLabel, 2, 0);
        formGrid.add(pricePerUnitField, 3, 0);
        formGrid.add(quantityLabel, 2, 1);
        formGrid.add(quantityField, 3, 1);
        formGrid.add(totalPriceLabel, 2, 2);
        formGrid.add(totalPriceField, 3, 2);
        formGrid.add(addToCartButton, 3, 3);

        return formGrid;
    }

    private void clearFormFields() {
        medicineIdField.clear();
    }

    private void setUpMedicineTable(TableView<Medicine> table) {
        TableColumn<Medicine, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Medicine, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Medicine, String> companyColumn = new TableColumn<>("Company");
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));

        TableColumn<Medicine, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Medicine, String> priceColumn = new TableColumn<>("Price/Unit");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(idColumn, nameColumn, companyColumn, quantityColumn, priceColumn);
        table.setPrefHeight(200);
    }

    private void fetchMedicineData(String keyword) {
        medicineTable.getItems().clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharma?useSSL=false", "root", "1234");
            Statement st = con.createStatement();
            String sql = "SELECT * FROM medicine WHERE name LIKE '%" + keyword + "%' OR id_medicine LIKE '%" + keyword + "%';";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                medicineTable.getItems().add(new Medicine(
                        rs.getString("id_medicine"),
                        rs.getString("name"),
                        rs.getString("company"),
                        rs.getString("quantity"),
                        rs.getString("price_unit")
                ));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class Medicine {
        private final String id;
        private final String name;
        private final String company;
        private final String quantity;
        private final String price;

        public Medicine(String id, String name, String company, String quantity, String price) {
            this.id = id;
            this.name = name;
            this.company = company;
            this.quantity = quantity;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCompany() {
            return company;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getPrice() {
            return price;
        }
    }
}
