package unsw.loopmania;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class DoggieCoin extends BasicItems{
    
    private static Double doggiePrice;

    private static DoggieCoin dCoinClass = null;

    private DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("DoggieCoin");
        setPrice(Math.floor(Math.random()*(16-2+1)+2));

    }

    /**
     * Takes in SimpleIntproperty x and SimpleIntproperty y in getInstance
     * method as this class is a Singleton pattern.
     * @param x
     * @param y
     * @return
     */

    public static synchronized DoggieCoin getInstance(SimpleIntegerProperty x, SimpleIntegerProperty y){

        if(dCoinClass == null){
            dCoinClass = new DoggieCoin(x, y);
            return dCoinClass;
        }
        return dCoinClass;
    }

    /**
     * Gives the create class or else will return null.
     * @return
     */

    public static DoggieCoin getDoggieCoin(){
        return dCoinClass;
    }

    /**
     * Increase the Price of DoggieCoin.
     */

    public void increasePrice(){
        doggiePrice = Math.floor(Math.random()*(160-100+1)+100);
        setPrice(doggiePrice);
    }

    /**
     * Decrease the price of DoggieCoin.
     */


    public void decreasePrice(){
        doggiePrice = Math.floor(Math.random()*(16-2+1)+2);
        setPrice(doggiePrice);
    }

    /**
     * Remove the instance of DoggieCoin.
     */

    public void removeDoggieCoin() {
        dCoinClass = null;
    }

}
