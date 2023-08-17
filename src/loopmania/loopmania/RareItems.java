package unsw.loopmania;

import java.util.HashMap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public abstract class RareItems extends Items implements DefenceStrategy, WeaponStrategy, RefillStrategy {
    
    public static final double price = 0.0;
    public RareItems confusingModeItem;
    private boolean refillable;

    public HashMap<String, Double> itemDamage = new HashMap<String, Double>();
    public HashMap<String, Double> defenceDamage = new HashMap<String, Double>();

    public RareItems(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setPrice(price);
        this.confusingModeItem = null;
    }

    public void setDamage(String key,Double value){
        itemDamage.put(key, value);
    }

    public void setDefence(String key,Double value) {
        defenceDamage.put(key, value);
    }

    public void setRefillable(boolean refill) {
        this.refillable = refill;
    }

    public boolean getRefillable() {
        return this.refillable;
    }

    public Double getDamage(String key){
        return itemDamage.get(key);
    }

    public Double getDefence(String key){
        return defenceDamage.get(key);
    }

    public void setConfusingModeItem(RareItems confusingModeItem) {
        this.confusingModeItem = confusingModeItem;
    }

    public RareItems getConfusingModeItem() {
        return confusingModeItem;   
    }

    public void characterHelp(Character curCharacter) {
        if (getRefillable()) {
            curCharacter.refillHealth();       
        }
    }

    @Override
    public void rareItemsHelp(Character curCharacter) {
        
        if (confusingModeItem != null) {
            this.characterHelp(curCharacter);
            confusingModeItem.rareItemsHelp(curCharacter);
        } else {
            this.characterHelp(curCharacter);
        }
    }

    @Override
    public double damageToEnemy(Enemy player) {
        
        double damage; 


        if (confusingModeItem != null) {
            damage = confusingModeItem.damageToEnemy(player) + this.getDamage(player.getType());

        } else {
            damage = this.getDamage(player.getType());
        }

        return damage;

    }

    @Override
    public double defendAgainstAttack(Enemy enemy) {

        double defence;


        if (confusingModeItem != null) {
            defence = confusingModeItem.defendAgainstAttack(enemy) + this.getDefence(enemy.getType());    
        } else {
            defence = getDefence(enemy.getType());
        }

        
        return defence;
    }
    
}
