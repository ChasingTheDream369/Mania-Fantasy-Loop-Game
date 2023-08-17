package unsw.loopmania;

import java.io.File;

import javafx.scene.image.Image;

public class AlliedSoldier extends Players{

    private static final double speed = 20;
    private AlliedSoldierState whenZombie;
    private AlliedSoldierState whenNotZombie;
    private AlliedSoldierState currenState;
    private PathPosition curPosition;
    private boolean isZombie;

    public AlliedSoldier(PathPosition position) {
        super(position);
        setSpeed(speed);
        whenZombie = new WhenZombie(position);
        whenNotZombie = new WhenNotZombie(this);
        currenState = whenNotZombie;
        this.curPosition = position;
        this.isZombie = false;
        setHealth(2);
        setDamage(3);
        setType("AlliedSoldier");
    }

    public void changeToZombie() {
        currenState = whenZombie;
    }
    
    
    /** 
     * attacks the player entity
     * @param player
     */
    public void attack(Players player) {
        currenState.allyAttack(player);
    }

    
    /** 
     * Returns the PathPosition
     * @return PathPosition
     */
    public PathPosition getPosition() {
        return curPosition;
    }

    
    /** 
     * Changes the allied soldier's iszombie
     * @param zombie
     */
    public void setIsZombie(boolean zombie) {
        this.isZombie = zombie;
    }

    
    /** 
     * Returns isZombie boolean
     * @return boolean
     */
    public boolean getIsZombie() {
        return isZombie;
    }
    
     /** 
     * Moves the allied soldier upPath
     * @return boolean
     */
    public void move(){
        moveUpPath();
    }

}