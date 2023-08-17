package unsw.loopmania;

public class Missile extends Players{

    public Missile(PathPosition position) {
        super(position);
        setType("Missile");
        setHealth(0.5);
        setDamage(2);
    }

    public void attack(Enemy e){
        e.reduceHealth(this);
    }

}
