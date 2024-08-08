package example;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Objects;

public class ProductEditDialog {
    private final ObservableList<Product> data;
    private final Product product;
    private final Stage dialog;
    private final Font font;
    private final GridPane root;
    private ButtonType result = ButtonType.CANCEL;

    private TextField nameEdit;
    private TextField makerEdit;
    private TextField priceEdit;
    private TextField descriptionEdit;

    public ProductEditDialog(Product product, ObservableList<Product> data) {
        this.data = data;
        this.product = product;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Product");
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        font = Font.font("Tahoma", FontWeight.NORMAL, 20);
        createName();
        createMaker();
        createPrice();
        createDescription();
        createButtons();
        Scene scene = new Scene(root, 600, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void createName() {
        Label nameProduct = new Label("Name:");
        nameProduct.setFont(font);
        root.add(nameProduct, 0, 1);
        nameEdit = new TextField();
        nameEdit.setFont(Font.font(24));
        nameEdit.setText(product.getName());
        root.add(nameEdit, 1, 1);
    }
    private void createMaker() {
        Label makerProduct = new Label("Maker:");
        makerProduct.setFont(font);
        root.add(makerProduct, 0, 2);
        makerEdit = new TextField();
        makerEdit.setFont(Font.font(24));
        makerEdit.setText(product.getMaker());
        root.add(makerEdit, 1, 2);
    }
    private void createPrice() {
        Label priceProduct = new Label("Price:");
        priceProduct.setFont(font);
        root.add(priceProduct, 0, 3);
        priceEdit = new TextField();
        priceEdit.setFont(Font.font(24));
        priceEdit.setText(Integer.toString(product.getPrice()));
        root.add(priceEdit, 1, 3);
    }
    private void createDescription() {
        Label descriptionProduct = new Label("Description:");
        descriptionProduct.setFont(font);
        root.add(descriptionProduct, 0, 4);
        descriptionEdit = new TextField();
        descriptionEdit.setFont(Font.font(24));
        descriptionEdit.setText(product.getDescription());
        root.add(descriptionEdit, 1, 4);
    }
    private void createButtons() {
        Button btnOk = new Button("Ok");
        btnOk.setFont(Font.font(24));
        root.add(btnOk, 0, 5);
        btnOk.setOnAction((ActionEvent e) -> {
            if (isNameAndMakerValid())
                if (isPriceValid())
                    if (isProductUnique())
                        handleOk();
                    else message3();
                else message2();
            else message1();
        });
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(Font.font(24));
        root.add(btnCancel, 1, 5);
        btnCancel.setOnAction((ActionEvent e) -> handleCancel());
    }

    private void message1(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Data entry error");
        alert.setHeaderText("Name and maker entry error");
        alert.setContentText("Name and maker must contain letters!!!");
        alert.showAndWait();
    }
    private void message2(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Data entry error");
        alert.setHeaderText("Price entry error");
        alert.setContentText("Price must be number!!!");
        alert.showAndWait();
    }
    private void message3(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Data entry error");
        alert.setHeaderText("Product entry error");
        alert.setContentText("Identical products!!!");
        alert.showAndWait();
    }

    private boolean isNameAndMakerValid() {
        return (nameEdit.getText().matches("[a-zA-Z]+") &&
                makerEdit.getText().matches("[a-zA-Z]+"));
    }
    private boolean isPriceValid() {
        return (priceEdit.getText().matches("[0-9]+"));
    }
    private boolean isProductUnique() {
        for (Product datum : data) {
            if (Objects.equals(datum.getName(), nameEdit.getText()) &&
                    Objects.equals(datum.getMaker(), makerEdit.getText()) &&
                    datum.getPrice() == Integer.parseInt(priceEdit.getText())) {
                return false;
            }
        }
        return true;
    }

    private void handleOk() {
        product.setName(nameEdit.getText());
        product.setMaker(makerEdit.getText());
        product.setPrice(Integer.parseInt(priceEdit.getText()));
        product.setDescription(descriptionEdit.getText());
        result = ButtonType.OK;
        dialog.close();
    }
    private void handleCancel() {
        result = ButtonType.CANCEL;
        dialog.close();
    }

    public ButtonType getResult() {
        return result;
    }
}