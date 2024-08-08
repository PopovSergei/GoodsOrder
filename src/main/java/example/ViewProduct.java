package example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ViewProduct {
    private Product product;
    private GridPane grid;
    private Text name;
    private Text maker;
    private Text price;
    private Text description;

    public ViewProduct(Product product) {
        createPane();
        setProduct(product);
    }

    private void createPane() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Font font = Font.font("Tahoma", FontWeight.NORMAL, 24);

        Label nameTitle = new Label("Name: ");
        nameTitle.setFont(font);
        grid.add(nameTitle, 0, 1);
        name = new Text();
        name.setFont(font);
        grid.add(name, 1, 1);

        Label makerTitle = new Label("Maker: ");
        makerTitle.setFont(font);
        grid.add(makerTitle, 0, 2);
        maker = new Text();
        maker.setFont(font);
        grid.add(maker, 1, 2);

        Label priceTitle = new Label("Price: ");
        priceTitle.setFont(font);
        grid.add(priceTitle, 0, 3);
        price = new Text();
        price.setFont(font);
        grid.add(price, 1, 3);

        Label descriptionTitle = new Label("Description: ");
        descriptionTitle.setFont(font);
        grid.add(descriptionTitle, 0, 4);
        description = new Text();
        description.setFont(font);
        grid.add(description, 1, 4);
    }

    private  void addListenersProduct(){
        name.textProperty().bind(product.nameProperty());
        maker.textProperty().bind(product.makerProperty());
        price.setText(Integer.toString(product.getPrice()));
        description.textProperty().bind(product.descriptionProperty());

        product.priceProperty().addListener((observable, oldValue, newValue) ->
                price.setText(Integer.toString(product.getPrice())));
    }

    public  GridPane getPane(){
        return  grid;
    }

    public void setProduct (Product product) {
        this.product = product;
        addListenersProduct();
    }
}