package unsw.loopmania;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public abstract class Enemy extends Players {
    
    private double battleRadius;
    private double supportRadius;
    private boolean changeToAlliedSoldier;
    public int currentAttackNum = 0;
    private double experienceLent;
     
    protected Items[] items = {new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new Armour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0))};
    
    protected Card[] cards = {new VampireCastleCard(new SimpleIntegerProperty(0),  new SimpleIntegerProperty(0)), 
        new ZombiePitCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new TowerCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new VillageCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new BarracksCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new TrapCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)), 
        new CampfireCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0))};
   
        public Image image;

    public Enemy(PathPosition position){
        super(position);
        changeToAlliedSoldier = false;
    }    

    /**
     * Gives the battle Radius
     * @return
     */

    public double getBattleRadius() {
        return battleRadius;
    }

    /**
     * set the battle radius
     * @param battleRadius
     */

    public void setBattleRadius(double battleRadius) {
        this.battleRadius = battleRadius;
    }

    /**
     * gives the support radius
     * @return
     */

    public double getSupportRadius() {
        return supportRadius;
    }

    /**
     * Set the support radius.
     * @param supportRadius
     */

    public void setSupportRadius(double supportRadius) {
        this.supportRadius = supportRadius;
    }

    /**
     * Attack the Player
     * @param player
     */

    public void attack(Players player){
        player.reduceHealth(this);
    }

    /**
     * When enemy turns back to enemy from allied soldier.
     */

    public void changeToEnemy(){
        changeToAlliedSoldier = false;
    }

    /**
     * When enemy becomes allied soldier.
     */
    public void changeToAlliedSoldier(){
        changeToAlliedSoldier = true;
    }

    /**
     * Checks if the enemy is allied soldier or not.
     * @return
     */

    public boolean isAlliedSoldier(){
        return changeToAlliedSoldier;
    }

    /**
     * Update The number of attack.
     */

    public void updateAttackNum(){
        this.currentAttackNum += 1;
    }

    /**
     * set the experience, which enemy gives to the character
     * after being defeated.
     * @param experienceLent
     */

    public void setExperience(double experienceLent) {
        this.experienceLent = experienceLent;
    }
    
    /**
     * Get the experience to give
     */

    public double lendExperience() {
        return experienceLent;
    }

    /**
     * attack by enemy.
     * @param player
     */

    abstract public void enemyAttack(Players player);

    /**
     * Movement of enemy.
     */

    public void move(){
        int directionChoice = (new Random()).nextInt(2);
        if (directionChoice == 0){
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
        }
    }


    /**
     * loots enemy gives to the character.
     * @return
     */

    public abstract GainsOnDefeat getGainsOnDefeat();

}
