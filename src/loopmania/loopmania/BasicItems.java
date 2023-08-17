package unsw.loopmania;
import java.util.HashMap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class BasicItems extends Items {

    public HashMap<String, Double> itemDamage = new HashMap<String, Double>();

    public BasicItems(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void setDamage(String key,Double value){
        itemDamage.put(key, value);
    }

    public Double getDamage(String key){
        return itemDamage.get(key);
    } 
    


}
