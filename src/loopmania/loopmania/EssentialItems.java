package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public abstract class EssentialItems extends Items {

    public Image image;

    /**
     * This class is for the Items which is alwasy need by 
     * the character like health potion and Gold. 
     * @param x
     * @param y
     */

    public EssentialItems(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        
    }
    
    /**
     * Getting the Image of the items.
     * @return
     */

    public Image getImage(){
        return image;
    }

    /**
     * function for the essentialItems.
     * @param character
     */

    abstract public void essentialStrategy(Character character);

}
