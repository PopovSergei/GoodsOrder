package example;

import java.io.*;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    private final ObservableList<Product> data = FXCollections.observableArrayList();
    private final ObservableList<Product> list = FXCollections.observableArrayList();
    private final TableView<Product> listTableView  = new TableView<>();
    private final TableView<Product> dataTableView  = new TableView<>();
    private final ViewProd view = new ViewProd();
    private String errorMessage="";
    private final Text FullPrice = new Text();
    private int fullPrice;

    private  void showMessage(String message) {
        Alert messageAlert = new Alert(Alert.AlertType.WARNING,message, ButtonType.OK);
        messageAlert.showAndWait();
    }

    private boolean isNameAndMakerValid(String name, String maker) {
        return (name.matches("[a-zA-Z ]+") &&
                maker.matches("[a-zA-Z ]+"));
    }
    private boolean isPriceValid(String price) {
        return (price.matches("[0-9]+"));
    }
    private boolean isProductUnique(String name, String maker, Integer price) {
        for (Product datum : data) {
            if (Objects.equals(datum.getName(), name) &&
                Objects.equals(datum.getMaker(), maker) &&
                datum.getPrice() == price) {
                return false;
            }
        }
        return true;
    }

    private void readDataFromFile(File dataFile) {
        try {
            data.clear();
            errorMessage="";
            BufferedReader in = new BufferedReader(new FileReader(dataFile));
            String str;
            while ((str = in.readLine()) != null) {
                try {
                    if(str.isEmpty()) break;
                    String[] dataArray = str.split(" +");
                    if (dataArray.length < 3)
                        throw new Exception("Not enough data");

                    String name = dataArray[0];
                    String maker = dataArray[1];
                    String price = dataArray[2];
                    StringBuilder description = new StringBuilder();
                    if (dataArray.length == 3) {
                        description.append("");
                    } else {
                        for (int i = 3; i < dataArray.length; ++i) {
                            description.append(dataArray[i]).append(" ");
                        }
                    }

                    if (isNameAndMakerValid(name, maker)) {
                        if (isPriceValid(price)) {
                            if (isProductUnique(name, maker, Integer.parseInt(price))) {
                                Product product = new Product(name, maker, Integer.parseInt(price), description.toString());
                                data.add(product);
                            } else {
                                throw new Exception("Identical products!!!");
                            }
                        } else {
                            throw new Exception("Price must be number!!!");
                        }
                    } else {
                        throw new Exception("Name and maker must contain letters!!!");
                    }
                } catch (Exception e){
                    errorMessage += e.getMessage()+"\n";
                    in.close();
                }
            }
            in.close();
        } catch (IOException e){
            errorMessage+=e.getMessage()+"\n";
        }
    }
    private void saveDataFile(File dataFile) {
        try {
            FileWriter out = new FileWriter(dataFile);
            for (Product product : data) {
                out.write(
                        product.getName() + " " +
                            product.getMaker() + " " +
                            product.getPrice() + " " +
                            product.getDescription() + "\n");
            }
            out.close();
        } catch (IOException e){
            showMessage(e.getMessage());
        }
    }
    private void saveListToFile(File dataFile) {
        try {
            FileWriter out = new FileWriter(dataFile);
            out.write(
                    "Full price: " + fullPrice + "\n" +
                        "Products:\n");
            for (Product product : list) {
                out.write(
                        " Name: " + product.getName() +
                            ", Maker: " + product.getMaker() +
                            ", Price: " + product.getPrice() + "\n");
            }
            out.close();
        } catch (IOException e){
            showMessage(e.getMessage());
        }
    }

    private void createDataTableView() {
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        nameCol.setMinWidth(110);

        TableColumn<Product, String> makerCol = new TableColumn<>("Maker");
        makerCol.setCellValueFactory(new PropertyValueFactory("maker"));
        makerCol.setMinWidth(110);

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory("price"));
        priceCol.setMinWidth(100);

        dataTableView.getColumns().setAll(nameCol, makerCol, priceCol);
    }
    private void createDataTable() {
        dataTableView.setItems(data);
        dataTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue!=null) view.setProd(newValue);
        });
        dataTableView.getSelectionModel().selectFirst();
    }
    private void createListTableView() {
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        nameCol.setMinWidth(110);

        TableColumn<Product, String> makerCol = new TableColumn<>("Maker");
        makerCol.setCellValueFactory(new PropertyValueFactory("maker"));
        makerCol.setMinWidth(110);

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory("price"));
        priceCol.setMinWidth(100);

        listTableView.getColumns().setAll(nameCol, makerCol, priceCol);
    }
    private void createListTable() {
        listTableView.setItems(list);
        listTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue != null)
                view.setProd(newValue);
        });
        listTableView.getSelectionModel().selectFirst();
    }

    private void handleFileOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        readDataFromFile(file);
        if(!errorMessage.isEmpty()) showMessage(errorMessage);
    }
    private void handleFileSave() {
        if (data.size() != 0) {
            saveDataFile(new File("src/main/resources/example/data.txt"));
        } else {
            showMessage("Empty data!");
        }
    }

    private void handleButtonEdit() {
        Product product = dataTableView.getSelectionModel().getSelectedItem();
        if (product != null) {
            ProductEditDialog productEditDialog = new ProductEditDialog(product, data);
        } else {
            showMessage("No selected item!");
        }
    }
    private void handleButtonAdd() {
        Product product = new Product("", "", 0, "");
        ProductEditDialog productEditDialog = new ProductEditDialog(product,  data);
        if (productEditDialog.getResult() == ButtonType.OK) {
            data.add(product);
        }
    }
    private void handleButtonDelete() {
        int selectedIndex = dataTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            dataTableView.getItems().remove(selectedIndex);
        } else {
            showMessage("No deleted item!");
        }
    }

    private void handleButtonShow() {
        Product product = dataTableView.getSelectionModel().getSelectedItem();
        if (product != null) {
            showProduct(product);
        } else {
            showMessage("No selected item!");
        }
    }
    private void showProduct(Product product) {
        Stage stage = new Stage();
        stage.setTitle("Show selected product");
        BorderPane pane = new BorderPane();
        ViewProduct viewProduct = new ViewProduct(product);
        pane.setCenter(viewProduct.getPane());
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void handleButtonAddToOrder() {
        int selectedIndex = dataTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            list.add(dataTableView.getSelectionModel().getSelectedItem());

            fullPrice += dataTableView.getSelectionModel().getSelectedItem().getPrice();
            FullPrice.setText("Full price: " + fullPrice);
        } else {
            showMessage("No selected item!");
        }
    }
    private void handleButtonDeleteOrder() {
        int selectedIndex = listTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            fullPrice -= listTableView.getSelectionModel().getSelectedItem().getPrice();
            FullPrice.setText("Full price: " + fullPrice);
            listTableView.getItems().remove(selectedIndex);
        } else {
            showMessage("No deleted item!");
        }
    }
    private void handleButtonNewOrder() {
        list.removeAll();
        listTableView.getItems().removeAll();
        fullPrice = 0;
        FullPrice.setText("Full price: " + fullPrice);
        listTableView.getItems().clear();
    }
    private void handleButtonSaveOrder() {
        if (list.size() != 0) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Data File");
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            saveListToFile(file);
        } else {
            showMessage("Empty list!");
        }
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        fileMenu.getItems().addAll(open, save, exit);
        open.setOnAction((ActionEvent event) -> handleFileOpen());
        save.setOnAction((ActionEvent event) -> handleFileSave());
        exit.setOnAction((ActionEvent event) ->Platform.exit());
        return fileMenu;
    }
    private Menu createEditMenu() {
        Menu editMenu = new Menu("Edit");

        MenuItem add = new MenuItem("Add product");
        editMenu.getItems().add(add);
        add.setOnAction((ActionEvent event) -> handleButtonAdd());

        MenuItem edit = new MenuItem("Edit product");
        editMenu.getItems().add(edit);
        edit.setOnAction((ActionEvent event) -> handleButtonEdit());

        MenuItem delete = new MenuItem("Delete product");
        editMenu.getItems().add(delete);
        delete.setOnAction((ActionEvent event) -> handleButtonDelete());

        return editMenu;
    }
    private Menu createShowMenu() {
        Menu showMenu = new Menu("Show");

        MenuItem showAnimal = new MenuItem("Show product");
        showAnimal.setOnAction((ActionEvent event) -> handleButtonShow());
        showMenu.getItems().add(showAnimal);

        return showMenu;
    }
    private Menu createOrderMenu() {
        Menu orderMenu = new Menu("Order");

        MenuItem saveOrder = new MenuItem("Save order");
        orderMenu.getItems().add(saveOrder);
        saveOrder.setOnAction((ActionEvent event) -> handleButtonSaveOrder());

        MenuItem add = new MenuItem("Add product to order");
        orderMenu.getItems().add(add);
        add.setOnAction((ActionEvent event) -> handleButtonAddToOrder());

        MenuItem delete = new MenuItem("Delete product from order");
        orderMenu.getItems().add(delete);
        delete.setOnAction((ActionEvent event) -> handleButtonDeleteOrder());

        MenuItem newOrder = new MenuItem("New order");
        orderMenu.getItems().add(newOrder);
        newOrder.setOnAction((ActionEvent event) -> handleButtonNewOrder());

        return orderMenu;
    }

    @Override
    public void init(){
        readDataFromFile(new File("src/main/resources/example/data.txt"));
    }

    @Override
    public void start(Stage primaryStage) {
        if(!errorMessage.isEmpty()) showMessage(errorMessage);
        primaryStage.setTitle("List of products");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));
        createDataTableView();
        createDataTable();
        createListTableView();
        createListTable();
        FullPrice.setText("Full price: " + fullPrice);
        FullPrice.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        root.setLeft(dataTableView);
        root.setCenter(view.getPane());
        root.setRight(listTableView);
        root.setBottom(FullPrice);
        BorderPane.setAlignment(FullPrice, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(FullPrice, new Insets(10,210,10,10));
        root.setTop(new MenuBar(createFileMenu(), createEditMenu(), createShowMenu(), createOrderMenu()));

        Scene scene = new Scene(root, 1000, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}