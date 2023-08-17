package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class ChickenWings extends StaticEntity implements PathItemStrategy{

    public ChickenWings(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);        
    }

    /**
     * Add chickenWings on path
     */
    @Override
    public void pathItemAdd(Character character) {
        character.setInvisible();
        //Runing Theard for remove the invisiblity after 10 sec.
        CheckTheInvisibilty invisibilty = new CheckTheInvisibilty(character);
        invisibilty.start();
    }
    
}
