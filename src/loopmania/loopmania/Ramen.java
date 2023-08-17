package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class Ramen extends StaticEntity implements PathItemStrategy{

    public Ramen(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void pathItemAdd(Character character) {
        character.changeToDragon();
    }
    
}
