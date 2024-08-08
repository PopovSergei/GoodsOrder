package example;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ViewProd {
    private StackPane pane;

    public ViewProd () {
        createPane();
    }

    private  void createPane(){
        pane = new StackPane();
        pane.setPadding(new Insets(5));
        Rectangle rect = new Rectangle(300, 300);
        rect.setFill(Color.WHITE);
        rect.setStrokeWidth(3);
        pane.getChildren().add(rect);
        Text textProd = new Text();
        textProd.setFont(Font.font(20));
        textProd.setWrappingWidth(270);
        pane.getChildren().add(textProd);
    }

    public void setProd(Product product) {
        ((Text)pane.getChildren().get(1)).setText(
                "Name: " + product.getName() + "\n" +
                "Maker: " + product.getMaker() + "\n" +
                "Price: " + product.getPrice() + "\n" +
                "Description: " + product.getDescription());
    }

    public StackPane getPane () {
        return pane;
    }
}