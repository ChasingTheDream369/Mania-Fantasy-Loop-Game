package unsw.loopmania;


import javafx.beans.property.SimpleIntegerProperty;

public class Factory {
	
    /**
     * This function creates the card with respect to the type.
     * @param type
     * @param x
     * @param y
     * @return
     */

    public static Card cardFactory(String type, SimpleIntegerProperty x, SimpleIntegerProperty y){
        if(type == null){
            return null;
        }		

        if(type.equalsIgnoreCase("BarracksCard")){
            return new BarracksCard(x, y);
          
        } else if(type.equalsIgnoreCase("CampfireCard")){
            return new CampfireCard(x, y);
          
        } else if(type.equalsIgnoreCase("TowerCard")){
            return new TowerCard(x, y);

        } else if(type.equalsIgnoreCase("TrapCard")){
            return new TrapCard(x, y);

        } else if(type.equalsIgnoreCase("VampireCastleCard")){
            return new VampireCastleCard(x, y);

        } else if(type.equalsIgnoreCase("VillageCard")){
            return new VillageCard(x, y);

        } else if(type.equalsIgnoreCase("ZombiePitCard")){
            return new ZombiePitCard(x, y);

        } 
     
       return null;
    }

    /**
     * This function creates the buildings with respect to the type.
     * @param type
     * @param x
     * @param y
     * @return
     */

    public static Buildings buildingsFactory(String type, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        if(type == null){
            return null;
        }		

        if(type.equalsIgnoreCase("BarracksCard")){
            return new BarracksBuilding(x, y);
          
        } else if(type.equalsIgnoreCase("CampfireCard")){
            return new CampfireBuilding(x, y);
          
        } else if(type.equalsIgnoreCase("TowerCard")){
            return new TowerBuilding(x, y);

        } else if(type.equalsIgnoreCase("TrapCard")){
            return new TrapBuilding(x, y);

        } else if(type.equalsIgnoreCase("VampireCastleCard")){
            return new VampireCastleBuilding(x, y);

        } else if(type.equalsIgnoreCase("VillageCard")){
            return new VillageBuilding(x, y);

        } else if(type.equalsIgnoreCase("ZombiePitCard")){
            return new ZombiePitBuilding(x, y);
        } 

       return null;
    }

    /**
     * Getting the Items with respect to the type.
     * @param type
     * @param x
     * @param y
     * @return
     */

    public static Items getItem(String type, int x, int y) {
        
        switch(type){
            case "Sword":
                return new Sword(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));

            case "Stake":
                return new Stake(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));

            case "Staff":
                return new Staff(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));

            case "Helmet":
                return new Helmet(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));

            case "Armour":
                return new Armour(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));

            case "Shield":
                return new Shield(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));
            
            case "TreeStump":
                return new TreeStump(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));
            
            case "AndurilFlameOfTheWest":
                return new AndurilFlameOfTheWest(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));
        
            case "HealthPotion":
                return new HealthPotion(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));
            
            default:
                return null;
        }
    }

    /**
     * Getting the price of Items with respect to the type.
     * @param type
     * @return
     */

    public static Integer getItemPrice(String type) {
        switch(type) {
            case "Sword":
                return 5;

            case "Stake":
                return 7;

            case "Staff":
                return 5;

            case "Helmet":
                return 5;

            case "Armour":
                return 7;

            case "Shield":
                return 7;

            case "HealthPotion":
                return 8;
            
            default:
                return 0;
        }
    }
}