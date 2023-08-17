package unsw.loopmania;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public abstract class Players extends MovingEntity {

    private String type;
    private DoubleProperty health;
    private double damage;

    public Players(PathPosition position) {
        super(position);
        this.health = new SimpleDoubleProperty();
    }

    public void reduceHealth(Players player){
        double newHealth = getHealth() - player.getDamage();
        setHealth(newHealth);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHealth() {
        return health.get();
    }

    public void setHealth(double health) {
        this.health.set(health);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isAlive() {
        return (this.getHealth() > 0);
    }

    public void setAlive(boolean alive) {
        if (!alive) {
            this.setHealth(0);
        }
    }
    
    public DoubleProperty getHealthProperty() {
        return health;
    }

}
