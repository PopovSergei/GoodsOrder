package example;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private StringProperty name;
    private StringProperty maker;
    private IntegerProperty price;
    private StringProperty description;

    public Product(String name, String maker, int price, String description) {
        nameProperty().set(name);
        makerProperty().set(maker);
        priceProperty().set(price);
        descriptionProperty().set(description);
    }

    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }
    public StringProperty makerProperty() {
        if (maker == null) {
            maker = new SimpleStringProperty();
        }
        return maker;
    }
    public IntegerProperty priceProperty() {
        if (price == null) {
            price = new SimpleIntegerProperty();
        }
        return price;
    }
    public StringProperty descriptionProperty() {
        if (description == null) {
            description = new SimpleStringProperty();
        }
        return description;
    }

    public final void setName(String value) {
        nameProperty().set(value);
    }
    public final void setMaker(String value) {
        makerProperty().set(value);
    }
    public final void setPrice(int value){
        priceProperty().set(value);
    }
    public final void setDescription(String value){
        descriptionProperty().set(value);
    }

    public final String getName() {
        return nameProperty().get();
    }
    public final String getMaker() {
        return makerProperty().get();
    }
    public final int getPrice() {
        return priceProperty().get();
    }
    public final String getDescription() {
        return descriptionProperty().get();
    }
}