package unsw.loopmania;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * a Card in the world
 * which doesn't move
 */
public abstract class Card extends StaticEntity {

    Image cardImage;
    String type;

    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void setType(String cardType) {
        this.type = cardType;
    }

    public String getType() {
        return type;
    }

    public void setX(int x) {
        x().set(x);
    }

    public void setY(int y) {
        y().set(y);
    }
    
}
