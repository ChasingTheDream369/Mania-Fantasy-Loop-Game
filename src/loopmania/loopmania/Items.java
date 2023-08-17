package unsw.loopmania;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Items extends StaticEntity {

    private String type;
    private DoubleProperty price;

    public Items(SimpleIntegerProperty x, SimpleIntegerProperty y) {  
        super(x, y);
        price = new SimpleDoubleProperty(0.0);
    }

    /**
     * Get the type of Item
     */

    public String getType() {
        return type;
    }

    /**
     * set the type of the Item.
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * setting the x value.
     * @param x
     */

    public void setX(int x) {
        x().set(x);
    }

    /**
     * setting the y value.
     */

    public void setY(int y) {
        y().set(y);
    }

    /**
     * set the price of the item
     * @param value
     */

    public void setPrice(Double value) {
        this.price.set(value);
    }

    /**
     * Get the price of the item.
     * @return
     */

    public DoubleProperty getPrice() {
        return price;
    }
}

