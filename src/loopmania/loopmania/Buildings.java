package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public abstract class Buildings extends StaticEntity {

    public String buildingType;
    public Image buildingImage;
    
    /**
     * creates the building of type buildingType
     * @param buildingType
     * @param x
     * @param y
     */
    public Buildings(String buildingType, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.buildingType = buildingType;
    }
    
    /**
     * returns the type of building
     */
    public String getType() {
        return this.buildingType;
    }
}
